package science.atlarge.opencraft.opencraft;

import java.util.ArrayDeque;
import java.util.Deque;

public class OverloadBreaker {
    private final Deque<Entry> rollingAverage = new ArrayDeque<>();

    public boolean trigger(long millis) {
        while (true) {
            Entry entry = rollingAverage.peekFirst();
            if (entry != null && entry.timestamp < System.currentTimeMillis() - 5000) {
                rollingAverage.removeFirst();
            } else {
                break;
            }
        }
        rollingAverage.add(new Entry(System.currentTimeMillis(), millis));
        return rollingAverage.stream().filter(e -> e.duration > 50).count() > rollingAverage.size() / 2;
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
