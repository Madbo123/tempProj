package com.mapper.map.bfst_map.Model.RTree;

import javafx.scene.canvas.GraphicsContext;

public interface HasBoundingBox {
    Bounds getBoundingBox();

    void draw(GraphicsContext graphicsContext);
}
