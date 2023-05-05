package com.mapper.map.bfst_map.Controller.AddressSearcher;

import java.util.HashMap;
import java.util.Map;

public class RadixTreeNode<Value> {
    private final Map<Character, RadixTreeNode<Value>> nextNodes;
    private String label;
    private Value value;

    public RadixTreeNode() {
        nextNodes = new HashMap<>();
    }

    public RadixTreeNode(String label, Value value) {
        nextNodes = new HashMap<>();
        this.label = label;
        this.value = value;
    }

    public void setNextNode(String label, Value value) {
        nextNodes.put(label.charAt(0), new RadixTreeNode<>(label, value));
    }

    public void setNextNode(RadixTreeNode<Value> node) {
        nextNodes.put(node.getLabel().charAt(0), node);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Map<Character, RadixTreeNode<Value>> getNextNodes() {
        return nextNodes;
    }

    public RadixTreeNode<Value> getNextNode(char character) {
        return nextNodes.get(character);
    }

    public String getLabel() {
        return label;
    }

    public Value getValue() {
        return value;
    }
}