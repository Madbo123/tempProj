package com.mapper.map.bfst_map.Model.Dijkstra;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedBag<Item> implements Iterable<Item> {
    private Node root;

    public LinkedBag() {
        root = null;
    }

    public void add(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Calling add(Item item) with null item.");
        }

        Node node = new Node();
        node.item = item;
        node.next = root;
        root = node;
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

        public LinkedIterator(Node root) {
            current = root;
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