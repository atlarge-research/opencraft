package net.glowstone;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import net.glowstone.util.config.BrokerConfig;

public class Benchmarker implements Closeable {

    public static class BenchMarkData {
        public long timeMilliseconds;
        public double relativeUtilization;
        public long playerCount;

        public BenchMarkData(long timeMilliseconds, long playerCount, double relativeUtilization) {
            this.timeMilliseconds = timeMilliseconds;
            this.playerCount = playerCount;
            this.relativeUtilization = relativeUtilization;
        }

        @Override
        public String toString() {
            return timeMilliseconds
                    +
                    "," + playerCount
                    +
                    "," + relativeUtilization
                    +
                    '\n';
        }
    }

    public final LinkedBlockingDeque<BenchMarkData> QUEUE = new LinkedBlockingDeque<>();

    private final String LOG_DIRECTORY = "benchmark_logs";
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final double NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);
    private final double TICKS_PER_SECOND = 20.0;
    private Thread thread;
    private String name;

    public Benchmarker() {
        name = "logs/generic_benchmark.csv";
        thread = new Thread(this::run);
    }

    public void run() {
        Path path = Paths.get(LOG_DIRECTORY + "/" + name);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("timestamp, playercount, relative utilization \n");
            while (running.get()) {
                BenchMarkData data;
                while ((data = QUEUE.poll()) != null) {
                    writer.write(data.toString());
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBrokerConfig(BrokerConfig brokerConfig) {
        String broker = brokerConfig.getType().toString();
        String channel = brokerConfig.getChannel().toString();
        String async = brokerConfig.getAsync() ? "_async_" : "";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String date = LocalDateTime.now().format(dateTimeFormatter).substring(0, 19);
        name = "results" + "_" + broker + "_" + channel + async + date + ".csv";
    }

    public void submitTickData(long tickStart, long tickEnd, long playerCount) {
        double relativeUtilization = (tickEnd - tickStart) * TICKS_PER_SECOND / NANOS_PER_SECOND;
        BenchMarkData benchMarkData = new BenchMarkData(
                tickEnd,
                playerCount,
                relativeUtilization
        );
        QUEUE.offer(benchMarkData);
    }

    public void start() {
        thread.start();
        Path logDir = Paths.get(LOG_DIRECTORY);
        if (!Files.exists(logDir)) {
            try {
                Files.createDirectory(logDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Close the collector, ensuring that the CSV file is properly written to disk.
     */
    @Override
    public void close() {
        try {
            running.set(false);
            thread.join();
        } catch (InterruptedException exception) {
            throw new IllegalStateException("Failed to join collector thread", exception);
        }
    }
}
