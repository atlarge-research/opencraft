package science.atlarge.opencraft.opencraft.executor;

import org.jetbrains.annotations.NotNull;

/**
 * This class can be executed by a PriorityExecutor. The order in which this and other runnables are executed is based
 * on the priority of the runnable.
 */
public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {

    private double priority;

    /**
     * Update the priority of the PriorityRunnable. The priority of the runnable is used to determine the order in which
     * PriorityRunnables are executed.
     */
    public abstract void updatePriority();

    /**
     * Set the priority.
     *
     * @param priority The new priority to be used.
     */
    protected void setPriority(double priority) {
        this.priority = priority;
    }

    /**
     * Get the priority.
     *
     * @return The priority of the runnable.
     */
    protected double getPriority() {
        return priority;
    }

    @Override
    public int compareTo(@NotNull PriorityRunnable other) {
        return Double.compare(priority, other.priority);
    }
}
