package net.glowstone;

import com.grahamedgecombe.jterminal.JTerminal;
import jline.console.ConsoleReader;
import jline.console.completer.Completer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.fusesource.jansi.AnsiConsole;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.*;

/**
 * A meta-class to handle all logging and input-related console improvements.
 * Portions are heavily based on CraftBukkit.
 */
public final class ConsoleManager {

    private static final String CONSOLE_DATE = "HH:mm:ss";
    private static final String FILE_DATE = "yyyy/MM/dd HH:mm:ss";
    private static final Logger logger = Logger.getLogger("");

    private final GlowServer server;

    private ConsoleReader reader;
    private ConsoleCommandSender sender;
    private ConsoleHandler consoleHandler;

    private JFrame jFrame = null;
    private JTerminal jTerminal = null;
    private JTextField jInput = null;

    private boolean running = true;
    private boolean jLine = false;

    public ConsoleManager(GlowServer server) {
        this.server = server;

        // install Ansi code handler, which makes colors work on Windows
        AnsiConsole.systemInstall();

        for (Handler h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        // used until/unless gui is created
        consoleHandler = new FancyConsoleHandler();
        //consoleHandler.setFormatter(new DateOutputFormatter(CONSOLE_DATE));
        logger.addHandler(consoleHandler);

        // todo: why is this here?
        Runtime.getRuntime().addShutdownHook(new ServerShutdownThread());

        // reader must be initialized before standard streams are changed
        try {
            reader = new ConsoleReader();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception initializing console reader", ex);
        }
        reader.addCompleter(new CommandCompleter());

        // set system output streams
        System.setOut(new PrintStream(new LoggerOutputStream(Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(Level.WARNING), true));
    }

    public ConsoleCommandSender getSender() {
        return sender;
    }

    public void startGui() {
        JTerminalListener listener = new JTerminalListener();

        jFrame = new JFrame("Glowstone");
        jTerminal = new JTerminal();
        jInput = new JTextField(80) {
            @Override public void setBorder(Border border) {}
        };
        jInput.paint(null);
        jInput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        jInput.setBackground(Color.BLACK);
        jInput.setForeground(Color.WHITE);
        jInput.setMargin(new Insets(0, 0, 0, 0));
        jInput.addKeyListener(listener);

        JLabel caret = new JLabel("> ");
        caret.setFont(new Font("Monospaced", Font.PLAIN, 12));
        caret.setForeground(Color.WHITE);

        JPanel ipanel = new JPanel();
        ipanel.add(caret, BorderLayout.WEST);
        ipanel.add(jInput, BorderLayout.EAST);
        ipanel.setBorder(BorderFactory.createEmptyBorder());
        ipanel.setBackground(Color.BLACK);
        ipanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        ipanel.setSize(jTerminal.getWidth(), ipanel.getHeight());

        jFrame.getContentPane().add(jTerminal, BorderLayout.NORTH);
        jFrame.getContentPane().add(ipanel, BorderLayout.SOUTH);
        jFrame.addWindowListener(listener);
        jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        jFrame.setLocationRelativeTo(null);
        jFrame.pack();
        jFrame.setVisible(true);

        sender = new ColoredCommandSender();
        logger.removeHandler(consoleHandler);
        logger.addHandler(new StreamHandler(new TerminalOutputStream(), new DateOutputFormatter(CONSOLE_DATE)));
    }

    public void startConsole(boolean jLine) {
        this.jLine = jLine;

        sender = new ColoredCommandSender();
        Thread thread = new ConsoleCommandThread();
        thread.setName("ConsoleCommandThread");
        thread.setDaemon(true);
        thread.start();

        /*logger.removeHandler(consoleHandler);
        consoleHandler = new FancyConsoleHandler();
        consoleHandler.setFormatter(new DateOutputFormatter(CONSOLE_DATE));
        logger.addHandler(consoleHandler);*/
    }

    public void startFile(String logfile) {
        File parent = new File(logfile).getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            logger.warning("Could not create log folder: " + parent);
        }
        Handler fileHandler = new RotatingFileHandler(logfile);
        fileHandler.setFormatter(new DateOutputFormatter(FILE_DATE));
        logger.addHandler(fileHandler);
    }

    public void stop() {
        running = false;
        for (Handler handler : logger.getHandlers()) {
            handler.flush();
            handler.close();
        }
        if (jFrame != null) {
            jFrame.dispose();
        }
    }

    private String colorize(String string) {
        if (string.indexOf(ChatColor.COLOR_CHAR) < 0) {
            return string;  // no colors in the message
        } else if ((!jLine || !reader.getTerminal().isAnsiSupported()) && jTerminal == null) {
            return ChatColor.stripColor(string);  // color not supported
        } else {
            return string.replace(ChatColor.RED.toString(), "\033[1;31m")
                .replace(ChatColor.YELLOW.toString(), "\033[1;33m")
                .replace(ChatColor.GREEN.toString(), "\033[1;32m")
                .replace(ChatColor.AQUA.toString(), "\033[1;36m")
                .replace(ChatColor.BLUE.toString(), "\033[1;34m")
                .replace(ChatColor.LIGHT_PURPLE.toString(), "\033[1;35m")
                .replace(ChatColor.BLACK.toString(), "\033[0;0m")
                .replace(ChatColor.DARK_GRAY.toString(), "\033[1;30m")
                .replace(ChatColor.DARK_RED.toString(), "\033[0;31m")
                .replace(ChatColor.GOLD.toString(), "\033[0;33m")
                .replace(ChatColor.DARK_GREEN.toString(), "\033[0;32m")
                .replace(ChatColor.DARK_AQUA.toString(), "\033[0;36m")
                .replace(ChatColor.DARK_BLUE.toString(), "\033[0;34m")
                .replace(ChatColor.DARK_PURPLE.toString(), "\033[0;35m")
                .replace(ChatColor.GRAY.toString(), "\033[0;37m")
                .replace(ChatColor.WHITE.toString(), "\033[1;37m") +
                "\033[0m";
        }
    }

    private class CommandCompleter implements Completer {
        public int complete(final String buffer, int cursor, List<CharSequence> candidates) {
            try {
                List<String> completions = server.getScheduler().syncIfNeeded(new Callable<List<String>>() {
                    public List<String> call() throws Exception {
                        return server.getCommandMap().tabComplete(sender, buffer);
                    }
                });
                if (completions == null) {
                    return cursor;  // no completions
                }
                candidates.addAll(completions);

                // location to position the cursor at (before autofilling takes place)
                return buffer.lastIndexOf(' ') + 1;
            } catch (Throwable t) {
                logger.log(Level.WARNING, "Error while tab completing", t);
                return cursor;
            }
        }
    }

    private class ConsoleCommandThread extends Thread {
        @Override
        public void run() {
            String command = "";
            while (running) {
                try {
                    if (jLine) {
                        command = reader.readLine(">", null);
                    } else {
                        command = reader.readLine();
                    }

                    if (command == null || command.trim().length() == 0)
                        continue;

                    server.getScheduler().runTask(null, new CommandTask(command.trim()));
                } catch (CommandException ex) {
                    logger.log(Level.WARNING, "Exception while executing command: " + command, ex);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Error while reading commands", ex);
                }
            }
        }
    }

    private class ServerShutdownThread extends Thread {
        @Override
        public void run() {
            server.shutdown();
        }
    }

    private class CommandTask implements Runnable {
        private final String command;

        public CommandTask(String command) {
            this.command = command;
        }

        public void run() {
            server.dispatchCommand(sender, EventFactory.onServerCommand(sender, command).getCommand());
        }
    }

    private class ColoredCommandSender implements ConsoleCommandSender {
        private final PermissibleBase perm = new PermissibleBase(this);

        ////////////////////////////////////////////////////////////////////////
        // CommandSender

        public String getName() {
            return "CONSOLE";
        }

        public void sendMessage(String text) {
            server.getLogger().info(text);
        }

        public void sendMessage(String[] strings) {
            for (String line : strings) {
                sendMessage(line);
            }
        }

        public GlowServer getServer() {
            return server;
        }

        public boolean isOp() {
            return true;
        }

        public void setOp(boolean value) {
            throw new UnsupportedOperationException("Cannot change operator status of server console");
        }

        ////////////////////////////////////////////////////////////////////////
        // Permissible

        public boolean isPermissionSet(String name) {
            return perm.isPermissionSet(name);
        }

        public boolean isPermissionSet(Permission perm) {
            return this.perm.isPermissionSet(perm);
        }

        public boolean hasPermission(String name) {
            return perm.hasPermission(name);
        }

        public boolean hasPermission(Permission perm) {
            return this.perm.hasPermission(perm);
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
            return perm.addAttachment(plugin, name, value);
        }

        public PermissionAttachment addAttachment(Plugin plugin) {
            return perm.addAttachment(plugin);
        }

        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
            return perm.addAttachment(plugin, name, value, ticks);
        }

        public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
            return perm.addAttachment(plugin, ticks);
        }

        public void removeAttachment(PermissionAttachment attachment) {
            perm.removeAttachment(attachment);
        }

        public void recalculatePermissions() {
            perm.recalculatePermissions();
        }

        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return perm.getEffectivePermissions();
        }

        ////////////////////////////////////////////////////////////////////////
        // Conversable

        @Override
        public boolean isConversing() {
            return false;
        }

        @Override
        public void acceptConversationInput(String input) {

        }

        @Override
        public boolean beginConversation(Conversation conversation) {
            return false;
        }

        @Override
        public void abandonConversation(Conversation conversation) {

        }

        @Override
        public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {

        }

        @Override
        public void sendRawMessage(String message) {

        }
    }

    private static class LoggerOutputStream extends ByteArrayOutputStream {
        private final String separator = System.getProperty("line.separator");
        private final Level level;

        public LoggerOutputStream(Level level) {
            super();
            this.level = level;
        }

        @Override
        public synchronized void flush() throws IOException {
            super.flush();
            String record = this.toString();
            super.reset();

            if (record.length() > 0 && !record.equals(separator)) {
                logger.logp(level, "LoggerOutputStream", "log" + level, record);
            }
        }
    }

    private class FancyConsoleHandler extends ConsoleHandler {
        public FancyConsoleHandler() {
            setFormatter(new DateOutputFormatter(CONSOLE_DATE));
            setOutputStream(System.out);
        }

        @Override
        public synchronized void flush() {
            try {
                if (jLine) {
                    reader.print(ConsoleReader.RESET_LINE + "");
                    reader.flush();
                    super.flush();
                    try {
                        reader.drawLine();
                    } catch (Throwable ex) {
                        reader.getCursorBuffer().clear();
                    }
                    reader.flush();
                } else {
                    super.flush();
                }
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "I/O exception flushing console output", ex);
            }
        }
    }

    private static class RotatingFileHandler extends StreamHandler {
        private final SimpleDateFormat dateFormat;
        private final String template;
        private final boolean rotate;
        private String filename;

        public RotatingFileHandler(String template) {
            this.template = template;
            rotate = template.contains("%D");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            filename = calculateFilename();
            updateOutput();
        }

        private void updateOutput() {
            try {
                setOutputStream(new FileOutputStream(filename, true));
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Unable to open " + filename + " for writing", ex);
            }
        }

        private String calculateFilename() {
            return template.replace("%D", dateFormat.format(new Date()));
        }

        @Override
        public synchronized void publish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            super.publish(record);
            flush();
        }

        @Override
        public synchronized void flush() {
            if (rotate) {
                String newFilename = calculateFilename();
                if (!filename.equals(newFilename)) {
                    filename = newFilename;
                    logger.log(Level.INFO, "Log rotating to {0}...", filename);
                    updateOutput();
                }
            }
            super.flush();
        }
    }

    private class DateOutputFormatter extends Formatter {
        private final SimpleDateFormat date;

        public DateOutputFormatter(String pattern) {
            this.date = new SimpleDateFormat(pattern);
        }

        @Override
        @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
        public String format(LogRecord record) {
            StringBuilder builder = new StringBuilder();

            builder.append(date.format(record.getMillis()));
            builder.append(" [");
            builder.append(record.getLevel().getLocalizedName().toUpperCase());
            builder.append("] ");
            builder.append(colorize(formatMessage(record)));
            builder.append('\n');

            if (record.getThrown() != null) {
                StringWriter writer = new StringWriter();
                record.getThrown().printStackTrace(new PrintWriter(writer));
                builder.append(writer.toString());
            }

            return builder.toString();
        }
    }

    private class JTerminalListener implements WindowListener, KeyListener {
        public void windowOpened(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
        public void windowClosed(WindowEvent e) {}
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}

        public void windowClosing(WindowEvent e) {
            server.shutdown();
        }

        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n') {
                String command = jInput.getText().trim();
                if (command.length() > 0) {
                    server.getScheduler().scheduleSyncDelayedTask(null, new CommandTask(command));
                }
                jInput.setText("");
            }
        }
    }

    private class TerminalOutputStream extends ByteArrayOutputStream {
        private final String separator = System.getProperty("line.separator");

        @Override
        public synchronized void flush() throws IOException {
            super.flush();
            String record = this.toString();
            super.reset();

            if (record.length() > 0 && !record.equals(separator)) {
                jTerminal.print(record);
                jFrame.repaint();
            }
        }
    }
}