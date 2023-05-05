package com.mapper.map.bfst_map.Model.Dijkstra;

public class Graph {
    private final LinkedBag<IEdge>[] adjacent;
    private final int vertexAmount;

    public Graph(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("Calling Graph(int max) with negative integer.");
        }

        adjacent = (LinkedBag<IEdge>[]) new LinkedBag[max];
        vertexAmount = max;

        for (int v = 0; v < vertexAmount; v++) {
            adjacent[v] = new LinkedBag<>();
        }
    }

    public void addEdge(IEdge e) {
        if (e == null) {
            throw new IllegalArgumentException("Calling addEdge(IEdge e) with null edge.");
        }

        adjacent[e.getFrom()].add(e);
    }

    public int getVertexAmount() {
        return vertexAmount;
    }

    public Iterable<IEdge> getAdjacent(int v) {
        if (v < 0) {
            throw new IllegalArgumentException("Calling getAdjacent(int v) with negative integer.");
        }

        if (v > vertexAmount - 1) {
            throw new IllegalArgumentException("Calling getAdjacent(int v) with integer greater than capacity.");
        }

        return adjacent[v];
    }
}