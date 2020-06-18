package net.glowstone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static final ArrayBlockingQueue<BenchMarkData> QUEUE = new ArrayBlockingQueue<>(20);

    private static final String LOGNAME =  "benchmark_results_" + LocalDateTime.now().toString().substring(0,19) + ".csv";
    private static final File FILE = new File(LOGNAME);
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private static class Loader {
        static final Benchmarker INSTANCE = new Benchmarker();
    }

    private Benchmarker() {
        EXECUTOR.submit(() -> {
                            try {
                                FileWriter fileWriter = new FileWriter(FILE, true);
                                while (!Thread.interrupted()) {
                                    BenchMarkData data = QUEUE.poll();
                                    while (data != null) {
                                        fileWriter.write(data.toString());
                                        fileWriter.flush();
                                        data = QUEUE.poll();
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        fileWriter.close();
                                    }
                                }
                                fileWriter.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
        );
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
