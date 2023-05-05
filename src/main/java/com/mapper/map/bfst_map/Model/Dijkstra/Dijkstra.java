package com.mapper.map.bfst_map.Model.Dijkstra;

import java.util.NoSuchElementException;

public class Dijkstra {
    private int target;
    private final Graph graph;
    private final IEdge[] edgeTo;
    private final double[] weightTo;
    private final double[] distanceTo;
    private IndexPriorityQueue<Double> priorityQueue;

    public Dijkstra(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Calling Dijkstra(Graph graph) with null graph.");
        }

        target = -1;
        this.graph = graph;
        edgeTo = new IEdge[graph.getVertexAmount()];
        weightTo = new double[graph.getVertexAmount()];
        distanceTo = new double[graph.getVertexAmount()];
    }

    //Calculate the shortest path from source vertex to target vertex with a traversal type.
    public void shortestPath(int s, int t, String type) {
        if (s < 0) {
            throw new IllegalArgumentException("Calling shortestPath(int s) with negative integer.");
        }

        if (s > graph.getVertexAmount() - 1) {
            throw new IllegalArgumentException("Calling shortestPath(int s) with integer greater than capacity.");
        }

        if (t < 0) {
            throw new IllegalArgumentException("Calling shortestPath(int t) with negative integer..");
        }

        if (t > graph.getVertexAmount() - 1) {
            throw new IllegalArgumentException("Calling shortestPath(int t) with integer greater than capacity.");
        }

        reset();

        target = t;
        weightTo[s] = 0.0;
        distanceTo[s] = 0.0;
        priorityQueue.decreaseElement(s, weightTo[s]);

        while (!priorityQueue.isEmpty()) {
            int v = priorityQueue.deleteMinimum();

            for (IEdge edge : graph.getAdjacent(v)) {
                relax(edge, type);
            }

            if (target == v) {
                return;
            }
        }
    }

    private void reset() {
        priorityQueue = new IndexPriorityQueue<>(graph.getVertexAmount());
        for (int i = 0; i < graph.getVertexAmount(); i++) {
            weightTo[i] = Double.POSITIVE_INFINITY;
            distanceTo[i] = Double.POSITIVE_INFINITY;
            edgeTo[i] = null;

            priorityQueue.insert(i, weightTo[i]);
        }
    }

    private void relax(IEdge e, String type) {
        int v = e.getFrom();
        int w = e.getTo();

        double alternativeWeight = weightTo[v] + e.getWeight(type);
        double alternativeDistance = distanceTo[v] + e.getDistance();
        if (alternativeWeight < weightTo[w]) {
            weightTo[w] = alternativeWeight;
            distanceTo[w] = alternativeDistance;
            edgeTo[w] = e;
            priorityQueue.decreaseElement(w, weightTo[w] + heuristic(w, target));
        }
    }

    private double heuristic(int v, int t) {
        return 0.0;

        /*
        double[] vCoordinates = graph.getCoordinates(v);
        double[] tCoordinates = graph.getCoordinates(t);

        return Math.sqrt(Math.pow(vCoordinates[0] - tCoordinates[0], 2) + Math.pow(vCoordinates[1] - tCoordinates[1], 2));

         */
    }

    //Return whether there has been found a path to the target vertex.
    public boolean hasPath() {
        if (target == -1) {
            throw new NoSuchElementException("No calculated path.");
        }

        return weightTo[target] < Double.POSITIVE_INFINITY;
    }

    //Return the path to the target vertex.
    public Iterable<IEdge> getPath() {
        if (target == -1) {
            throw new NoSuchElementException("No calculated path.");
        }

        if (!hasPath()) {
            return null;
        }

        LinkedBag<IEdge> path = new LinkedBag<>();
        for (IEdge e = edgeTo[target]; e != null; e = edgeTo[e.getFrom()]) {
            path.add(e);
        }

        return path;
    }

    //Return the weight to the target vertex.
    public double getWeight() {
        if (target == -1) {
            throw new NoSuchElementException("No calculated path.");
        }

        return weightTo[target];
    }

    //Return the distance to the target vertex.
    public double getDistance() {
        if (target == -1) {
            throw new NoSuchElementException("No calculated path.");
        }

        return distanceTo[target];
    }
}