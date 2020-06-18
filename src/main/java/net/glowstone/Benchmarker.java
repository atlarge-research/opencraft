package net.glowstone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

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
                            while(!Thread.interrupted()) {
                                BenchMarkData data = QUEUE.poll();
                                if (data != null) {
                                    try {
                                        FileWriter fileWriter = new FileWriter(FILE, true);
                                        fileWriter.write(data.toString());
                                        fileWriter.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

        );
    }

    public static Benchmarker getInstance() {
        return Loader.INSTANCE;
    }

    public void submitTickData(BenchMarkData benchMarkData) {
        try {
            QUEUE.put(benchMarkData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
