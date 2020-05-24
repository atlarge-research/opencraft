package net.glowstone.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The sortable blocking queue is a blocking queue implementation that allows for its elements to be sorted.
 *
 * @param <Element> the type of elements that can be stored in the queue.
 */
public final class SortableBlockingQueue<Element> implements BlockingQueue<Element> {

    private final Comparator<Element> comparator;
    private final List<Element> elements;
    private final Lock readLock;
    private final Lock writeLock;
    private final Condition notEmpty;

    /**
     * Create a sortable blocking queue.
     *
     * @param comparator the comparator that should be used for sorting elements.
     */
    public SortableBlockingQueue(Comparator<Element> comparator) {
        this.comparator = comparator.reversed();
        elements = new ArrayList<>();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
        notEmpty = writeLock.newCondition();
    }

    /**
     * Update the queue by adding and removing elements and then sorting them.
     *
     * @param additions the elements that should be added to the queue.
     * @param removals the elements that should be removed from the queue.
     */
    public void update(Collection<Element> additions, Collection<Element> removals) {
        writeLock.lock();
        try {
            removeAll(removals);
            addAll(additions);
            sort();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Sort the elements in the queue.
     */
    public void sort() {
        writeLock.lock();
        try {
            elements.sort(comparator);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean add(@NotNull Element element) {
        writeLock.lock();
        try {
            elements.add(element);
            if (elements.size() == 1) {
                notEmpty.signalAll();
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean offer(@NotNull Element element) {
        writeLock.lock();
        try {
            elements.add(element);
            if (elements.size() == 1) {
                notEmpty.signalAll();
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Element remove() {
        writeLock.lock();
        try {
            if (elements.isEmpty()) {
                throw new NoSuchElementException();
            }
            int index = elements.size() - 1;
            return elements.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Element poll() {
        writeLock.lock();
        try {
            if (elements.isEmpty()) {
                return null;
            }
            int index = elements.size() - 1;
            return elements.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Element element() {
        readLock.lock();
        try {
            if (elements.isEmpty()) {
                throw new NoSuchElementException();
            }
            int index = elements.size() - 1;
            return elements.get(index);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public Element peek() {
        readLock.lock();
        try {
            if (elements.isEmpty()) {
                return null;
            }
            int index = elements.size() - 1;
            return elements.get(index);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void put(@NotNull Element element) {
        writeLock.lock();
        try {
            elements.add(element);
            if (elements.size() == 1) {
                notEmpty.signalAll();
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean offer(Element element, long timeout, @NotNull TimeUnit unit) {
        writeLock.lock();
        try {
            elements.add(element);
            if (elements.size() == 1) {
                notEmpty.signalAll();
            }
            return true;
        } finally {
            writeLock.unlock();
        }
    }

    @NotNull
    @Override
    public Element take() throws InterruptedException {
        writeLock.lock();
        try {
            while (elements.isEmpty()) {
                notEmpty.await();
            }
            int index = elements.size() - 1;
            return elements.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Nullable
    @Override
    public Element poll(long timeout, @NotNull TimeUnit unit) throws InterruptedException {
        writeLock.lock();
        try {
            while (elements.isEmpty()) {
                notEmpty.await();
                // TODO: Add timeout
            }
            int index = elements.size() - 1;
            return elements.remove(index);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean remove(Object object) {
        writeLock.lock();
        try {
            return elements.remove(object);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> collection) {
        readLock.lock();
        try {
            return elements.containsAll(collection);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Element> collection) {
        writeLock.lock();
        try {
            boolean shouldSignal = elements.isEmpty();
            boolean changed = elements.addAll(collection);
            shouldSignal &= !elements.isEmpty();
            if (shouldSignal) {
                notEmpty.signalAll();
            }
            return changed;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> collection) {
        writeLock.lock();
        try {
            return elements.removeAll(collection);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean removeIf(Predicate<? super Element> predicate) {
        writeLock.lock();
        try {
            return elements.removeIf(predicate);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> collection) {
        writeLock.lock();
        try {
            return elements.retainAll(collection);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void clear() {
        writeLock.lock();
        try {
            elements.clear();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int size() {
        readLock.lock();
        try {
            return elements.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        readLock.lock();
        try {
            return elements.isEmpty();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean contains(Object object) {
        readLock.lock();
        try {
            return elements.contains(object);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Returns an iterator over the elements in this collection.  There are no guarantees concerning the order in which
     * the elements are returned (unless this collection is an instance of some class that provides a guarantee). The
     * returned iterator is not thread-safe.
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    @NotNull
    @Override
    public Iterator<Element> iterator() {
        return elements.iterator();
    }

    @Override
    public void forEach(Consumer<? super Element> consumer) {
        readLock.lock();
        try {
            elements.forEach(consumer);
        } finally {
            readLock.unlock();
        }
    }

    @NotNull
    @Override
    public Object[] toArray() {
        readLock.lock();
        try {
            return elements.toArray();
        } finally {
            readLock.unlock();
        }
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] ts) {
        readLock.lock();
        try {
            //noinspection SuspiciousToArrayCall
            return elements.toArray(ts);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public int drainTo(@NotNull Collection<? super Element> collection) {
        writeLock.lock();
        try {
            int count = elements.size();
            collection.addAll(elements);
            elements.clear();
            return count;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public int drainTo(@NotNull Collection<? super Element> collection, int max) {
        writeLock.lock();
        try {
            int count = 0;
            for (int index = elements.size() - 1; index >= 0 && count < max; index--) {
                Element element = elements.remove(index);
                collection.add(element);
                count++;
            }
            return count;
        } finally {
            writeLock.unlock();
        }
    }
}
