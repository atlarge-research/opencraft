package net.glowstone.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriorityRunnableTest {

    private TestPriorityRunnable lowPriorityRunnable;
    private TestPriorityRunnable highPriorityRunnable;

    @BeforeEach
    void beforeEach() {
        lowPriorityRunnable = new TestPriorityRunnable(1);
        highPriorityRunnable = new TestPriorityRunnable(2);
    }

    @Test
    void updatePriority() {
        lowPriorityRunnable.updatePriority();
        assertEquals(2, lowPriorityRunnable.getPriority());
        lowPriorityRunnable.updatePriority();
        assertEquals(4, lowPriorityRunnable.getPriority());
    }

    @Test
    void compareTo() {
        assertTrue(lowPriorityRunnable.compareTo(highPriorityRunnable) < 0);
        lowPriorityRunnable.updatePriority();
        assertEquals(0, lowPriorityRunnable.compareTo(highPriorityRunnable));
        lowPriorityRunnable.updatePriority();
        assertTrue(lowPriorityRunnable.compareTo(highPriorityRunnable) > 0);
    }

    @Test
    void run() {
        assertEquals(0, lowPriorityRunnable.executedCount);
        lowPriorityRunnable.run();
        assertEquals(1, lowPriorityRunnable.executedCount);
        lowPriorityRunnable.run();
        assertEquals(2, lowPriorityRunnable.executedCount);
    }

    private static final class TestPriorityRunnable extends PriorityRunnable {

        public int executedCount;

        public TestPriorityRunnable(double priority) {
            executedCount = 0;
            setPriority(priority);
        }

        @Override
        public void updatePriority() {
            setPriority(getPriority() * 2);
        }

        @Override
        public void run() {
            executedCount++;
        }
    }
}
