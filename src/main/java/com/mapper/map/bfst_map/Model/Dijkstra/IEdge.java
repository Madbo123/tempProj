package com.mapper.map.bfst_map.Model.Dijkstra;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public interface IEdge {
    int getFrom();
    int getTo();
    int getSpeed();
    double getDistance();
    double getWeight(String type);
    void draw(GraphicsContext graphicsContext);
}