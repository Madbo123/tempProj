package com.mapper.map.bfst_map.Controller.AddressSearcher;

public class RadixTree<Value> {
    private final RadixTreeNode<Value> root;

    public RadixTree() {
        root = new RadixTreeNode<>();
    }

    public void put(String key, Value value) {
        if (key == null) {
            throw new IllegalArgumentException("Calling put(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling put(String key) with empty string.");
        }

        put(root, 0, key, value);
    }

    private void put(RadixTreeNode<Value> currentNode, int currentIndex, String key, Value value) {
        char currentCharacter = key.charAt(currentIndex);
        String currentString = key.substring(currentIndex);
        RadixTreeNode<Value> nextNode = currentNode.getNextNode(currentCharacter);

        if (nextNode == null) {
            currentNode.setNextNode(currentString, value);
            return;
        }

        int splitIndex = getFirstMismatchLetter(currentString, nextNode.getLabel());
        if (splitIndex == -1) {
            if (currentString.length() == nextNode.getLabel().length()) {
                nextNode.setValue(value);
                return;
            } else if (currentString.length() < nextNode.getLabel().length()) {
                String suffix = nextNode.getLabel().substring(currentString.length());
                nextNode.setLabel(suffix);
                currentNode.setNextNode(currentString, value);
                RadixTreeNode<Value> newNextNode = currentNode.getNextNode(currentCharacter);
                newNextNode.setNextNode(nextNode);
                return;
            } else {
                splitIndex = nextNode.getLabel().length();
            }
        } else {
            String suffix = nextNode.getLabel().substring(splitIndex);
            nextNode.setLabel(suffix);
            currentNode.setNextNode(currentString.substring(0, splitIndex), null);
            RadixTreeNode<Value> newNextNode = currentNode.getNextNode(currentCharacter);
            newNextNode.setNextNode(nextNode);
            newNextNode.setNextNode(currentString.substring(splitIndex), value);
            return;
        }

        put(nextNode, currentIndex + splitIndex, key, value);
    }

    private int getFirstMismatchLetter(String word, String edgeWord) {
        int length = Math.min(word.length(), edgeWord.length());
        for (int i = 1; i < length; i++) {
            if (word.charAt(i) != edgeWord.charAt(i)) {
                return i;
            }
        }

        return -1;
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Calling get(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling get(String key) with empty string.");
        }

        RadixTreeNode<Value> node = get(root, 0, key);

        if (node == null) {
            return null;
        }

        return node.getValue();
    }

    private RadixTreeNode<Value> get(RadixTreeNode<Value> currentNode, int currentIndex, String key) {
        if (key.length() == currentIndex) {
            return currentNode;
        }

        if (key.length() < currentIndex) {
            return null;
        }

        char currentCharacter = key.charAt(currentIndex);
        String currentString = key.substring(currentIndex);
        RadixTreeNode<Value> nextNode = currentNode.getNextNode(currentCharacter);

        if (nextNode == null) {
            return null;
        }

        if (!currentString.startsWith(nextNode.getLabel())) {
            return null;
        }

        return get(nextNode, currentIndex + nextNode.getLabel().length(), key);
    }

    private RadixTreeNode<Value> getFloor(RadixTreeNode<Value> currentNode, int currentIndex, String key) {
        if (key.length() == currentIndex) {
            return currentNode;
        }

        if (key.length() < currentIndex) {
            return null;
        }

        char currentCharacter = key.charAt(currentIndex);
        String currentString = key.substring(currentIndex);
        RadixTreeNode<Value> nextNode = currentNode.getNextNode(currentCharacter);

        if (nextNode == null) {
            return null;
        }

        if (!currentString.startsWith(nextNode.getLabel())) {
            return currentNode;
        }

        return getFloor(nextNode, currentIndex + nextNode.getLabel().length(), key);
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("Calling contains(String key) with null string.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Calling contains(String key) with empty string.");
        }

        return get(key) != null;
    }

    public Iterable<String> keys(int amount) {
        if (amount <= 0) {
            amount = Integer.MAX_VALUE;
        }

        LinkedQueue<String> queue = new LinkedQueue<>();
        collect(root, new StringBuilder(), queue, amount);
        return queue;
    }

    public Iterable<String> keysWithPrefix(String prefix, int amount) {
        if (prefix == null) {
            throw new IllegalArgumentException("Calling keysWithPrefix(String prefix) with null string.");
        }

        if (prefix.length() == 0) {
            throw new IllegalArgumentException("Calling keysWithPrefix(String prefix) with empty string.");
        }

        if (amount <= 0) {
            amount = Integer.MAX_VALUE;
        }

        LinkedQueue<String> queue = new LinkedQueue<>();
        RadixTreeNode<Value> node = getFloor(root, 0, prefix);
        if (node == null) {
            return queue;
        }

        if (get(root, 0, prefix) == null) {
            //collect(node, new StringBuilder(prefix), queue, amount);
        } else {
            collect(node, new StringBuilder(prefix), queue, amount);
        }

        return queue;
    }

    private void collect(RadixTreeNode<Value> node, StringBuilder prefix, LinkedQueue<String> queue, int amount) {
        if (node == null) {
            return;
        }

        if (queue.size() >= amount) {
            return;
        }

        if (node.getValue() != null) {
            queue.enqueue(prefix.toString());
        }

        for (RadixTreeNode<Value> nextNode : node.getNextNodes().values()) {
            collect(nextNode, prefix.append(nextNode.getLabel()), queue, amount);
            prefix.delete(prefix.length() - nextNode.getLabel().length(), prefix.length());
        }
    }
}