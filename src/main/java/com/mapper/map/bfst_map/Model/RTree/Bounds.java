package com.mapper.map.bfst_map.Model.RTree;

public class Bounds {
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;

    public Bounds() {}

    public Bounds(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public boolean overlaps(Bounds other){
        // If one rectangle is on left side of other
        if (minX > other.getMaxX() || other.getMinX() > maxX) {
            return false;
        }

        // If one rectangle is above other
        if (minY > other.getMaxY() || other.getMinY() > maxY) {
            return false;
        }

        return true;
    }
}
