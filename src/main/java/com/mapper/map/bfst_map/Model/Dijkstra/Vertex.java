package com.mapper.map.bfst_map.Model.Dijkstra;

public class Vertex {
    private int roadCount;

    public Vertex() {
        roadCount = 1;
    }

    public void addRoad(){
        roadCount++;
    }

    public int getRoadCount(){
        return roadCount;
    }
}