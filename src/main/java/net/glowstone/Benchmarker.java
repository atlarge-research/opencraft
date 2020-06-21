package net.glowstone;

import net.glowstone.util.config.BrokerConfig;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class Benchmarker implements Closeable{
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
            return timeMilliseconds +
                    "," + playerCount +
                    "," + relativeUtilization +
                    '\n';
        }
    }

    public static final LinkedBlockingDeque<BenchMarkData> QUEUE = new LinkedBlockingDeque<>(20);


    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Thread thread;
    private final Path path;

    public Benchmarker(BrokerConfig brokerConfig) {
        String broker = brokerConfig.getType().toString();
        String channel = brokerConfig.getChannel().toString();
        String async = brokerConfig.getAsync() ? "_async" : "";
        String logName =  "results" + "_" + broker + "_" + channel + async + LocalDateTime.now().toString().substring(0,17) + ".csv";
        path = Paths.get(logName);
        thread = new Thread(() -> {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
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
        });
    }

    public void submitTickData(long tickStart, long tickEnd, long playerCount) {
        double relativeUtilization = ((tickEnd-tickStart)/ 50.00) * 100.0;
        BenchMarkData benchMarkData = new BenchMarkData(
                tickEnd,
                playerCount,
                relativeUtilization
        );
        QUEUE.offer(benchMarkData);
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

    public void start() {
        thread.start();
    }
}
