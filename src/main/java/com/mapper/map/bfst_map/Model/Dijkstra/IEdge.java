package com.mapper.map.bfst_map.Model.Dijkstra;

import javafx.scene.canvas.GraphicsContext;

public interface IEdge {
    int getFrom();
    int getTo();
    double getDistance();
    double getWeight(String type);
    float[] getCoordinates();
    void draw(GraphicsContext graphicsContext);
}