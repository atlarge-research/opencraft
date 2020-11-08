package science.atlarge.opencraft.opencraft.measurements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventFileLogger extends EventLogger {

    private final static int PERIOD_MS = 1000;

    private final File file;
    private PrintWriter writer;
    private final BlockingQueue<String> logEntries = new LinkedBlockingDeque<>();
    private final Timer timer = new Timer(true);

    public EventFileLogger(File file) {
        this.file = file;
    }

    @Override
    public void init() throws IOException {
        if (!this.file.isFile()) {
            this.file.createNewFile();
        }
        this.writer = new PrintWriter(file);
        this.writer.println("timestamp\tkey\tvalue");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                flush();
            }
        }, PERIOD_MS, PERIOD_MS);
    }

    @Override
    public synchronized void log(String key, String value) {
        try {
            logEntries.put(System.currentTimeMillis() + "\t" + key + "\t" + value);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public synchronized void flush() {
        for (int i = 0; i < logEntries.size(); i++) {
            try {
                writer.println(logEntries.take());
            } catch (InterruptedException e) {
                break;
            } finally {
                writer.flush();
            }
        }
    }

    @Override
    public void close() throws Exception {
        timer.cancel();
        writer.close();
    }
}
