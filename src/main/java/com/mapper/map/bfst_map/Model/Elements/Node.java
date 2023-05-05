package com.mapper.map.bfst_map.Model.Elements;

import java.io.Serializable;

public class Node implements Serializable {
    float lat, lon;

    public Node(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getX() {
        return (0.56F * lon);
    }

    public float getY() {
        return -lat;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }
}