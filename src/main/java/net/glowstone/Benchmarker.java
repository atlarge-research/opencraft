package net.glowstone;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

public class Benchmarker {
    public static class BenchMarkData {
        public long timeMilliseconds;
        public double relativeUtilization;
        public long playerCount;

        public BenchMarkData(long timeMilliseconds, double relativeUtilization, long playerCount) {
            this.timeMilliseconds = timeMilliseconds;
            this.relativeUtilization = relativeUtilization;
            this.playerCount = playerCount;
        }

        @Override
        public String toString() {
            return timeMilliseconds +
                    "," + relativeUtilization +
                    "," + playerCount +
                    '\n';
        }
    }

    public static final LinkedBlockingDeque<BenchMarkData> QUEUE = new LinkedBlockingDeque<>(20);

    private static final String LOGNAME =  "benchmark_results_" + LocalDateTime.now().toString().substring(0,19) + ".csv";
    private static final Path PATH = Paths.get(LOGNAME);
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private static class Loader {
        static final Benchmarker INSTANCE = new Benchmarker();
    }

    private Benchmarker() {
        Thread thread = new Thread(() -> {
            try (BufferedWriter writer = Files.newBufferedWriter(PATH)) {
                while (running.get()) {
                    BenchMarkData data;
                    while ((data = QUEUE.poll()) != null) {
                        writer.write(data.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        );
        thread.start();
    }

    public static Benchmarker getInstance() {
        return Loader.INSTANCE;
    }

    public void submitTickData(long tickStart, long tickEnd, long playerCount) {
        double relativeUtilization = ((tickEnd-tickStart)/ 50.00) * 100.0;
        BenchMarkData benchMarkData = new BenchMarkData(
                tickEnd,
                relativeUtilization,
                playerCount
        );
        try {
            QUEUE.put(benchMarkData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
