package science.atlarge.opencraft.opencraft.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The sortable blocking queue is tested for basic queue functionality and the ability to be sorted.
 */
class SortableBlockingQueueTest {

    private SortableBlockingQueue<Integer> queue;

    /**
     * Construct a queue for each test using the default Integer.compareTo method.
     */
    @BeforeEach
    void beforeEach() {
        queue = new SortableBlockingQueue<>(Integer::compareTo);
    }

    /**
     * Verify that the queue is sorted in ascending order.
     */
    @Test
    void sortTest() {
        queue.offer(3);
        queue.offer(1);
        queue.offer(2);
        queue.sort();
        assertEquals(1, queue.poll());
        assertEquals(2, queue.poll());
        assertEquals(3, queue.poll());
        assertTrue(queue.isEmpty());
    }

    /**
     * Verify that an element can be added to the queue.
     */
    @Test
    void addTest() {
        assertTrue(queue.add(1));
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that an element can be offered to the queue.
     */
    @Test
    void offerTest() {
        assertTrue(queue.offer(1));
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that an element can be removed from the queue.
     */
    @Test
    void removeTest() {
        queue.add(1);
        assertEquals(1, queue.remove());
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that removing an element from an empty queue throws the correct exception.
     */
    @Test
    void removeEmptyTest() {
        assertThrows(NoSuchElementException.class, queue::remove);
    }

    /**
     * Verify that an element can be polled from the queue.
     */
    @Test
    void pollTest() {
        queue.add(1);
        assertEquals(1, queue.poll());
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that polling an empty queue returns null.
     */
    @Test
    void pollEmptyTest() {
        assertNull(queue.poll());
    }

    /**
     * Verify that the next element in the queue can be retrieved.
     */
    @Test
    void elementTest() {
        queue.add(1);
        assertEquals(1, queue.element());
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that retrieving the next element from an empty queue throws the correct exception.
     */
    @Test
    void elementEmptyTest() {
        assertThrows(NoSuchElementException.class, queue::element);
    }

    /**
     * Verify that the next element in the queue can be peeked.
     */
    @Test
    void peekTest() {
        queue.add(1);
        assertEquals(1, queue.peek());
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that peeking the next element of an empty queue returns null.
     */
    @Test
    void peekEmptyTest() {
        assertNull(queue.peek());
    }

    /**
     * Verify that an element can be put in to the queue.
     */
    @Test
    void putTest() {
        queue.put(1);
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that an element can be offered to the queue with a timeout.
     */
    @Test
    void offerTimeoutTest() {
        queue.offer(1, 50L, TimeUnit.MILLISECONDS);
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that an element can be taken out of the queue.
     *
     * @throws InterruptedException thrown whenever the take method is interrupted by another thread.
     */
    @Test
    void takeTest() throws InterruptedException {
        queue.add(1);
        assertEquals(1, queue.take());
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that an element can be taken out of the queue, even when it's not already there.
     *
     * @throws InterruptedException thrown when the element could not be taken out of the queue.
     * @throws ExecutionException thrown whenever the take method is interrupted by another thread.
     */
    @Test
    void takeWaitTest() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                latch.countDown();
                int element = queue.take();
                future.complete(element);
            } catch (InterruptedException exception) {
                future.completeExceptionally(exception);
            }
        }).start();
        latch.await();
        TimeUnit.MILLISECONDS.sleep(50);
        queue.add(1);
        assertEquals(1, future.get());
    }

    /**
     * Verify that an element can be polled from the queue within a certain time limit.
     *
     * @throws InterruptedException thrown whenever the poll method is interrupted by another thread.
     */
    @Test
    void pollTimeoutTest() throws InterruptedException {
        queue.add(1);
        assertEquals(1, queue.poll(50L, TimeUnit.MILLISECONDS));
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that an element can be polled from the queue even when its not already there.
     *
     * @throws InterruptedException thrown whenever the element cannot be retrieved.
     * @throws ExecutionException thrown whenever the poll method is interrupted by another thread.
     */
    @Test
    void pollTimeoutWaitTest() throws InterruptedException, ExecutionException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                latch.countDown();
                Integer element = queue.poll(100L, TimeUnit.MILLISECONDS);
                future.complete(element);
            } catch (InterruptedException exception) {
                future.completeExceptionally(exception);
            }
        }).start();
        latch.await();
        TimeUnit.MILLISECONDS.sleep(50L);
        queue.add(1);
        assertEquals(1, future.get());
    }

    /**
     * Verify that polling an empty queue with a timeout returns null.
     *
     * @throws InterruptedException thrown whenever the poll method is interrupted by another thread.
     */
    @Test
    void pollTimeoutEmptyTest() throws InterruptedException {
        assertNull(queue.poll(50L, TimeUnit.MILLISECONDS));
    }

    /**
     * Verify that there is no limit on the capacity of the queue.
     */
    @Test
    void remainingCapacityTest() {
        assertEquals(queue.remainingCapacity(), Integer.MAX_VALUE);
    }

    /**
     * Verify that an arbitrary object can be removed from the queue.
     */
    @Test
    void removeObjectTest() {
        queue.add(1);
        assertTrue(queue.remove(1));
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that removing an object, which is not in the queue, from the queue does not throw an exception.
     */
    @Test
    void removeNonExistingObjectTest() {
        assertFalse(queue.remove(1));
    }

    /**
     * Verify that removing one object does not also remove another.
     */
    @Test
    void removeCorrectObjectTest() {
        queue.add(1);
        queue.add(2);
        queue.remove(1);
        assertTrue(queue.contains(2));
    }

    /**
     * Verify that the containment of multiple values can be checked at once.
     */
    @Test
    void containsAllTest() {
        queue.add(1);
        queue.add(2);
        assertTrue(queue.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that the containsAll method correctly recognizes it when none of the elements is contained.
     */
    @Test
    void containsNotAllTest() {
        queue.add(1);
        assertFalse(queue.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that multiple elements can be added at once.
     */
    @Test
    void addAllTest() {
        queue.addAll(Arrays.asList(1, 2));
        assertTrue(queue.contains(1));
        assertTrue(queue.contains(2));
    }

    /**
     * Verify that multiple elements can be added at once to an already filled queue.
     */
    @Test
    void addMoreTest() {
        queue.addAll(Arrays.asList(1, 2));
        queue.addAll(Arrays.asList(3, 4));
        assertTrue(queue.contains(1));
        assertTrue(queue.contains(2));
        assertTrue(queue.contains(3));
        assertTrue(queue.contains(4));
    }

    /**
     * Verify that multiple elements can be removed at once.
     */
    @Test
    void removeAllTest() {
        queue.add(1);
        queue.add(2);
        assertTrue(queue.removeAll(Arrays.asList(1, 2)));
        assertFalse(queue.contains(1));
        assertFalse(queue.contains(2));
    }

    /**
     * Verify that removing elements that do not exist does not cause an exception to be thrown.
     */
    @Test
    void removeAllNoneTest() {
        assertFalse(queue.removeAll(Collections.singleton(1)));
    }

    /**
     * Verify that removing some items does not remove all items.
     */
    @Test
    void removeAllCorrectTest() {
        queue.add(1);
        queue.add(2);
        queue.removeAll(Collections.singleton(1));
        assertFalse(queue.contains(1));
        assertTrue(queue.contains(2));
    }

    /**
     * Verify that elements can be removed via a predicate.
     */
    @Test
    void removeIfTest() {

        queue.add(1);
        queue.add(2);

        assertTrue(queue.removeIf(element -> element == 1));
        assertFalse(queue.contains(1));
        assertTrue(queue.contains(2));

        assertEquals(2, queue.poll());
        assertNull(queue.poll());
    }

    /**
     * Verify that not matching the predicate does not cause an exception to be thrown.
     */
    @Test
    void removeIfNoneTest() {
        assertFalse(queue.removeIf(element -> element == 1));
    }

    /**
     * Verify that elements can be retained.
     */
    @Test
    void retainAllTest() {
        queue.add(1);
        assertFalse(queue.retainAll(Collections.singleton(1)));
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that all elements are removed when none are in the list of retained values.
     */
    @Test
    void retainAllNoneTest() {
        queue.add(1);
        assertTrue(queue.retainAll(Collections.singleton(2)));
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that the correct elements are removed when they are not in the list of retained values.
     */
    @Test
    void retainAllCorrectTest() {
        queue.add(1);
        queue.add(2);
        assertTrue(queue.retainAll(Collections.singleton(1)));
        assertTrue(queue.contains(1));
        assertFalse(queue.contains(2));
    }

    /**
     * Verify that the queue can be cleared.
     */
    @Test
    void clearTest() {
        queue.add(1);
        queue.clear();
        //noinspection ConstantConditions
        assertTrue(queue.isEmpty());
    }

    /**
     * Verify that the size method correctly represents the state of the queue.
     */
    @Test
    void sizeTest() {
        assertEquals(0, queue.size());
        queue.add(1);
        assertEquals(1, queue.size());
        queue.addAll(Arrays.asList(2, 3));
        assertEquals(3, queue.size());
        queue.remove();
        assertEquals(2, queue.size());
        queue.clear();
        assertEquals(0, queue.size());
    }

    /**
     * Verify whether the queue is initially empty.
     */
    @Test
    void emptyTest() {
        assertTrue(queue.isEmpty());
    }

    /**
     * Verify that the queues emptiness is correctly updated.
     */
    @Test
    void notEmptyTest() {
        queue.add(1);
        assertFalse(queue.isEmpty());
    }

    /**
     * Verify that the containment of an element can be checked.
     */
    @Test
    void containsTest() {
        queue.add(1);
        assertTrue(queue.contains(1));
    }

    /**
     * Verify that the non-existence of an element can be checked.
     */
    @Test
    void notContainsTest() {
        assertFalse(queue.contains(1));
    }

    /**
     * Verify that the queue can be iterated.
     */
    @Test
    void iteratorTest() {
        queue.add(1);
        queue.add(2);
        Iterator<Integer> iterator = queue.iterator();
        Collection<Integer> elements = new ArrayList<>();
        while (iterator.hasNext()) {
            int element = iterator.next();
            elements.add(element);
        }
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that the queue can be iterated via the forEach method.
     */
    @Test
    void forEachTest() {
        queue.add(1);
        queue.add(2);
        Collection<Integer> elements = new ArrayList<>();
        //noinspection UseBulkOperation
        queue.forEach(elements::add);
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that the queue can be converted into an array.
     */
    @Test
    void toArrayTest() {
        queue.add(1);
        queue.add(2);
        Object[] objects = queue.toArray();
        Collection<Object> elements = Arrays.stream(objects).collect(Collectors.toList());
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that the queue can be converted into a typed array.
     */
    @Test
    void toTypedArrayTest() {
        queue.add(1);
        queue.add(2);
        Integer[] integers = queue.toArray(new Integer[0]);
        Collection<Integer> elements = Arrays.stream(integers)
                .collect(Collectors.toList());
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that draining an empty queue does not cause an exception to be thrown.
     */
    @Test
    void drainEmptyToTest() {
        Collection<Integer> elements = new ArrayList<>();
        queue.drainTo(elements);
        assertTrue(elements.isEmpty());
    }

    /**
     * Verify that all the elements in the queue can be drained to an external collection.
     */
    @Test
    void drainAllToTest() {
        queue.add(1);
        queue.add(2);
        Collection<Integer> elements = new ArrayList<>();
        assertEquals(2, queue.drainTo(elements));
        assertTrue(queue.isEmpty());
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that the maximum number of elements can be drained from the queue.
     */
    @Test
    void drainToMaxedTest() {
        queue.add(1);
        queue.add(2);
        Collection<Integer> elements = new ArrayList<>();
        assertEquals(2, queue.drainTo(elements, 2));
        assertTrue(queue.isEmpty());
        assertTrue(elements.containsAll(Arrays.asList(1, 2)));
    }

    /**
     * Verify that only a number of elements can be drained from the queue.
     */
    @Test
    void drainSomeToTest() {
        queue.add(1);
        queue.add(2);
        Collection<Integer> elements = new ArrayList<>();
        assertEquals(1, queue.drainTo(elements, 1));
        assertEquals(1, queue.size());
        assertEquals(1, elements.size());
    }

    /**
     * Verify that a transaction is performed as a single atomic operation.
     */
    @Test
    void transactionTest() {

        CountDownLatch first = new CountDownLatch(1);

        new Thread(() -> queue.transaction(queue -> {
            try {
                queue.offer(1);
                first.countDown();
                TimeUnit.MILLISECONDS.sleep(50L);
                queue.poll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })).start();

        try {
            first.await();
            assertNull(queue.poll());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
