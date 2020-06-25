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
import net.glowstone.util.config.BrokerType;
import net.glowstone.util.config.ChannelType;

public class Benchmarker implements Closeable {

    public static class BenchMarkData {

        private final long timestamp;
        private final long players;
        private final double relativeUtilization;

        public BenchMarkData(long timestamp, long players, double relativeUtilization) {
            this.timestamp = timestamp;
            this.players = players;
            this.relativeUtilization = relativeUtilization;
        }

        @Override
        public String toString() {
            return timestamp + "," + players + "," + relativeUtilization + '\n';
        }
    }

    private static final double NANOS_PER_SECOND = TimeUnit.SECONDS.toNanos(1);
    private static final double TICKS_PER_SECOND = 20.0;

    private final LinkedBlockingDeque<BenchMarkData> queue = new LinkedBlockingDeque<>();
    private final AtomicBoolean running;
    private final Thread thread;
    private String name;

    public Benchmarker() {
        name = "generic";
        thread = new Thread(this::run);
        running = new AtomicBoolean(true);
    }

    public void run() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String date = LocalDateTime.now().format(dateTimeFormatter);
        Path path = Paths.get(name + "_SPECIAL_" + date + ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("timestamp,players,relative_utilization\n");
            while (running.get()) {
                BenchMarkData data;
                while ((data = queue.poll()) != null) {
                    writer.write(data.toString());
                    writer.flush();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void setName(BrokerConfig config) {
        BrokerType type = config.getType();
        name = type.toString();
        if (type != BrokerType.ACTIVEMQ && type != BrokerType.RABBITMQ) {
            ChannelType channelType = config.getChannel();
            name += "_" + channelType.toString();
        }
        if (config.getAsync()) {
            name += "_async";
        }
    }

    public void submitTickData(long playerCount, long tickStart, long tickEnd) {
        double relativeUtilization = (tickEnd - tickStart) * TICKS_PER_SECOND / NANOS_PER_SECOND;
        BenchMarkData benchMarkData = new BenchMarkData(
                tickEnd,
                playerCount,
                relativeUtilization
        );
        queue.offer(benchMarkData);
    }

    public void start() {
        thread.start();
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
