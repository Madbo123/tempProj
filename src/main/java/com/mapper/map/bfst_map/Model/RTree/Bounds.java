package com.mapper.map.bfst_map.Model.RTree;

public class Bounds {
    private float minX;
    private float minY;
    private float maxX;
    private float maxY;

    public Bounds() {}

    public Bounds(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public boolean overlaps(Bounds other){
        // If one rectangle is on left side of other
        if (minX > other.getMaxX() || other.getMinX() > maxX) {
            return false;
        }

        // If one rectangle is above other
        return !(minY > other.getMaxY()) && !(other.getMinY() > maxY);
    }
}
