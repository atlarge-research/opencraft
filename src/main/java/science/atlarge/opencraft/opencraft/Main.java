package science.atlarge.opencraft.opencraft;

import science.atlarge.opencraft.opencraft.i18n.ConsoleMessages;

public class Main {

    /**
     * Creates a new server on TCP port 25565 and starts listening for connections.
     *
     * @param args The command-line arguments.
     */
    public static void main(String... args) {
        try {
            GlowServer server = GlowServer.createFromArguments(args);

            // we don't want to run a server when called with --version, --help or --generate-config
            if (server == null) {
                return;
            }
            if (GlowServer.getGenerateConfigOnly()) {
                ConsoleMessages.Info.CONFIG_ONLY_DONE.log();
                return;
            }

            server.run();
        } catch (SecurityException e) {
            ConsoleMessages.Error.CLASSPATH.log(e);
        } catch (Throwable t) {
            // general server startup crash
            ConsoleMessages.Error.STARTUP.log(t);
            System.exit(1);
        }
    }
}
