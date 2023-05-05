package com.mapper.map.bfst_map.Controller.AddressSearcher;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedQueue<Item> implements Iterable<Item> {
    private Node root;
    private Node leaf;
    private int n;

    public LinkedQueue() {
        root = null;
        leaf = null;
        n = 0;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Calling enqueue(Item item) with null item.");
        }

        Node oldLast = leaf;
        leaf = new Node();
        leaf.item = item;
        leaf.next = null;

        if (isEmpty()) {
            root = leaf;
        } else {
            oldLast.next = leaf;
        }

        n++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        Item item = root.item;
        root = root.next;
        n--;

        if (isEmpty()) {
            leaf = null;
        }

        return item;
    }

    public Item peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty.");
        }

        return root.item;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return n;
    }

    private class Node {
        private Item item;
        private Node next;
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator(root);
    }

    private class LinkedIterator implements Iterator<Item> {
        private Node current;

        public LinkedIterator(Node first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Linked iterator is empty.");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}