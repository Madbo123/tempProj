package com.mapper.map.bfst_map.Controller.AddressSearcher;

public class TernarySearchTree<Value> {
    private Node root;
    private int n;

    public TernarySearchTree() {
        root = null;
        n = 0;
    }

    //Inserts key-value pair into the ternary search tree.
    public void put(String key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Calling put(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling put(String key) with empty string.");
        }

        if (!contains(key)) {
            n++;
        } else if (value == null) {
            n--;
        }

        root = put(root, key, value, 0);
    }

    private Node put(Node node, String key, Value value, int index) {
        char c = key.charAt(index);

        if (node == null) {
            node = new Node();
            node.c = c;
        }

        if (node.c > c) {
            node.left = put(node.left, key, value, index);
        } else if (node.c < c) {
            node.right = put(node.right, key, value, index);
        } else if (key.length() - 1 > index) {
            node.middle = put(node.middle, key, value, index + 1);
        } else {
            node.value = value;
        }

        return node;
    }

    //Return the value paired with the key.
    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Calling get(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling get(String key) with empty string.");
        }

        Node node = get(root, key, 0);

        if (node == null) {
            return null;
        }

        return node.value;
    }

    private Node get(Node node, String key, int index) {
        char c = key.charAt(index);

        if (node == null) {
            return null;
        }

        if (node.c > c) {
            return get(node.left, key, index);
        } else if (node.c < c) {
            return get(node.right, key, index);
        } else if (key.length() - 1 > index) {
            return get(node.middle, key, index + 1);
        }

        return node;
    }

    //Return whether the ternary search tree contains the key.
    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Calling contains(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling contains(String key) with empty string.");
        }

        return get(key) != null;
    }

    //Return amount of keys in the ternary search tree.
    public int size() {
        return n;
    }

    //Return all keys.
    public Iterable<String> keys(int amount) {
        if (amount == 0) {
            amount = Integer.MAX_VALUE;
        }

        LinkedQueue<String> queue = new LinkedQueue<>();
        collect(root, new StringBuilder(), queue, amount);
        return queue;
    }

    //Return all keys that start with the same letters as the prefix.
    public Iterable<String> keysWithPrefix(String prefix, int amount) {
        if (prefix == null) {
            throw new IllegalArgumentException("Calling keysWithPrefix(String prefix) with null string.");
        }

        if (prefix.length() == 0) {
            throw new IllegalArgumentException("Calling keysWithPrefix(String prefix) with empty string.");
        }

        if (amount == 0) {
            amount = Integer.MAX_VALUE;
        }

        LinkedQueue<String> queue = new LinkedQueue<>();
        Node node = get(root, prefix, 0);

        if (node == null) {
            return queue;
        }

        if (node.value != null) {
            queue.enqueue(prefix);
        }

        collect(node.middle, new StringBuilder(prefix), queue, amount);
        return queue;
    }

    private void collect(Node node, StringBuilder prefix, LinkedQueue<String> queue, int amount) {
        if (node == null) {
            return;
        }

        if (queue.size() >= amount) {
            return;
        }

        collect(node.left, prefix, queue, amount);

        if (node.value != null) {
            queue.enqueue(prefix.toString() + node.c);
        }

        collect(node.middle, prefix.append(node.c), queue, amount);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(node.right, prefix, queue, amount);
    }

    private class Node {
        private char c;
        private Node left;
        private Node middle;
        private Node right;
        private Value value;
    }
}