package science.atlarge.opencraft.opencraft;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Server;

public class OverloadBreaker {

    private final long serverStart = System.currentTimeMillis();
    private final Deque<Entry> rollingAverage = new ArrayDeque<>();
    private final Server server;

    public OverloadBreaker(Server server) {
        this.server = server;
    }

    public boolean trigger(long millis) {
        long now = System.currentTimeMillis();
        while (true) {
            Entry entry = rollingAverage.peekFirst();
            if (entry != null && entry.timestamp < now - 5000) {
                rollingAverage.removeFirst();
            } else {
                break;
            }
        }
        rollingAverage.add(new Entry(now, millis));
        return server.getOnlinePlayers().size() > 0
                && rollingAverage.stream().filter(e -> e.duration > 50).count() > rollingAverage.size() / 2;
    }

    public List<Double> load(long periodMillis) {
        long start = System.currentTimeMillis() - periodMillis;
        return rollingAverage.stream()
                .filter(e -> e.timestamp >= start)
                .mapToDouble(e -> e.duration)
                .boxed()
                .collect(Collectors.toList());
    }

    public boolean overloaded(long periodMillis, double load) {
        if (System.currentTimeMillis() - serverStart < 10_000) {
            return false;
        }
        long start = System.currentTimeMillis() - periodMillis;
        return rollingAverage.stream()
                .filter(e -> e.timestamp >= start)
                .mapToDouble(e -> e.duration)
                .map(d -> d > 50 * load ? 1 : 0)
                .average()
                .orElse(0)
                > 0.5;
    }

    private static class Entry {
        private final long timestamp;
        private final long duration;

        private Entry(long timestamp, long duration) {
            this.timestamp = timestamp;
            this.duration = duration;
        }
    }
}
