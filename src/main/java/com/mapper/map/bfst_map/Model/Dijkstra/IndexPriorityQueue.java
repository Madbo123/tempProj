package com.mapper.map.bfst_map.Model.Dijkstra;

import java.util.NoSuchElementException;

public class IndexPriorityQueue<Element extends Comparable<Element>> {
    private final int max;
    private int n;
    private final int[] pq;
    private final int[] qp;
    private final Element[] elements;

    //Constructs the index priority queue.
    public IndexPriorityQueue(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Calling MinimumPriorityQueue(int size) with negative integer.");
        }

        this.max = max;

        n = 0;
        pq = new int[max + 1];
        qp = new int[max + 1];
        elements = (Element[]) new Comparable[max + 1];

        for (int i = 0; i <= max; i++) {
            qp[i] = -1;
        }
    }

    //Inserts an element into the heap.
    public void insert(int i, Element element) {
        if (i < 0) {
            throw new IllegalArgumentException("Calling insert(int i) with negative integer.");
        }

        if (i >= max) {
            throw new IllegalArgumentException("Calling insert(int i) with integer greater then capacity.");
        }

        if (contains(i)) {
            throw new IllegalArgumentException("Calling insert(int i) with existing integer.");
        }

        if (element == null) {
            throw new IllegalArgumentException("Calling insert(Element element) with null element.");
        }

        n++;
        qp[i] = n;
        pq[n] = i;
        elements[i] = element;
        swim(n);
    }

    //Decreases the weight of an element and swim it up the heap.
    public void decreaseElement(int i, Element element) {
        if (i < 0) {
            throw new IllegalArgumentException("Calling decreaseElement(int i) with negative integer.");
        }

        if (i >= max) {
            throw new IllegalArgumentException("Calling decreaseElement(int i) with integer greater then capacity.");
        }

        if (!contains(i)) {
            throw new IllegalArgumentException("Calling decreaseElement(int i) with non-existing integer.");
        }

        if (elements[i].compareTo(element) == 0) {
            throw new IllegalArgumentException("Calling decreaseElement(Element element) with equal element.");
        }

        if (elements[i].compareTo(element) < 0) {
            throw new IllegalArgumentException("Calling decreaseElement(Element element) with greater element.");
        }

        elements[i] = element;
        swim(qp[i]);
    }

    //Return whether the index priority queue contains the element.
    public boolean contains(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Calling contains(int i) with negative integer.");
        }

        if (i >= max) {
            throw new IllegalArgumentException("Calling contains(int i) with integer greater then capacity.");
        }

        return qp[i] != -1;
    }

    //Delete the smallest element and return the element.
    public int deleteMinimum() {
        if (isEmpty()) {
            throw new NoSuchElementException("Index priority queue is empty.");
        }

        int min = pq[1];
        exchange(1, n--);
        sink(1);
        qp[min] = -1;
        elements[min] = null;
        pq[n + 1] = -1;
        return min;
    }

    //Return whether the index priority queue is empty.
    public boolean isEmpty() {
        return n == 0;
    }

    //Swim element up the heap to satisfy the heap property.
    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exchange(k, k / 2);
            k = k / 2;
        }
    }

    //Sink element down the heap to satisfy the heap property.
    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;

            if (j < n && greater(j, j + 1)) {
                j++;
            }

            if (!greater(k, j)) {
                break;
            }

            exchange(k, j);
            k = j;
        }
    }

    //Return whether element 'i' is greater then element 'j'.
    private boolean greater(int i, int j) {
        return elements[pq[i]].compareTo(elements[pq[j]]) > 0;
    }

    //Exchanges two elements in the index priority queue.
    private void exchange(int i, int j) {
        int temporary = pq[i];
        pq[i] = pq[j];
        pq[j] = temporary;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }
}