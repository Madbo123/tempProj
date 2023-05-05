package com.mapper.map.bfst_map.Controller.AddressSearcher;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class QueueTest {
    private LinkedQueue<Integer> queue;

    @BeforeEach
    public void setUp() {
        queue = new LinkedQueue<>();

        queue.enqueue(1);
        queue.enqueue(5);
        queue.enqueue(7);
        queue.enqueue(3);
        queue.enqueue(9);
    }

    @Test
    public void enqueueWithNormal() {
        String message = "";

        try {
            queue.enqueue(4);
        } catch (Exception e) {
            message = e.getMessage();
        }

        assertEquals("", message);
    }

    @Test
    public void enqueueWithNull() {
        String message = "";

        try {
            queue.enqueue(null);
        } catch (Exception e) {
            message = e.getMessage();
        }

        assertEquals("Calling enqueue(Item item) with null item.", message);
    }

    @Test
    public void dequeueWithEmptyQueue() {
        String message = "";

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        try {
            queue.dequeue();
        } catch (Exception e) {
            message = e.getMessage();
        }

        assertEquals("Queue is empty.", message);
    }

    @Test
    public void dequeueWithNotEmptyQueue() {
        assertEquals(1, queue.dequeue());
        assertEquals(5, queue.dequeue());
        assertEquals(7, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(9, queue.dequeue());
    }

    @Test
    public void peekWithEmptyQueue() {
        String message = "";

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        try {
            queue.peek();
        } catch (Exception e) {
            message = e.getMessage();
        }

        assertEquals("Queue is empty.", message);
    }

    @Test
    public void peekWithNotEmptyQueue() {
        assertEquals(1, queue.peek());
    }

    @Test
    public void isEmptyWithEmptyQueue() {
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        assertTrue(queue.isEmpty());
    }

    @Test
    public void isEmptyWithNotEmptyQueue() {
        assertFalse(queue.isEmpty());
    }

    @Test
    public void sizeWithFive() {
        assertEquals(5, queue.size());
    }

    @Test
    public void sizeAfterEnqueue() {
        queue.enqueue(4);

        assertEquals(6, queue.size());
    }

    @Test
    public void sizeAfterDequeue() {
        queue.dequeue();

        assertEquals(4, queue.size());
    }

    @Test
    public void iteratorWithEmptyQueue() {
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        List<Integer> items = new ArrayList<>();
        for (Integer item : queue) {
            items.add(item);
        }

        assertEquals(0, items.size());
    }

    @Test
    public void iteratorWithNotEmptyQueue() {
        List<Integer> items = new ArrayList<>();
        for (Integer item : queue) {
            items.add(item);
        }

        assertEquals(1, items.get(0));
        assertEquals(5, items.get(1));
        assertEquals(7, items.get(2));
        assertEquals(3, items.get(3));
        assertEquals(9, items.get(4));
    }
}