package com.mapper.map.bfst_map.Model.Dijkstra;

import com.mapper.map.bfst_map.Model.Elements.Node;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import com.mapper.map.bfst_map.Utils.Highway;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Road extends Waypoint implements IEdge, HasBoundingBox {
    private final int from;
    private final int to;
    private int speed;
    private double distance;
    private double weight;
    private Bounds boundingBox;
    private Set<String> types;
    private final float[] coordinates;

    public Road(int from, int to, List<Node> nodes, Map<String, String> tags) {
        super(tags);

        if (from < 0) {
            throw new IllegalArgumentException("Calling Edge(int from) with negative integer.");
        }

        if (to < 0) {
            throw new IllegalArgumentException("Calling Edge(int to) with negative integer.");
        }

        if (nodes == null) {
            throw new IllegalArgumentException("Calling Edge(Node[] nodes) with null array.");
        }

        if (nodes.size() == 0) {
            throw new IllegalArgumentException("Calling Edge(Node[] nodes) with empty array.");
        }

        if (tags == null) {
            throw new IllegalArgumentException("Calling Edge(Map<String, String> tags) with null map.");
        }

        if (tags.size() == 0) {
            throw new IllegalArgumentException("Calling Edge(Map<String, String> tags) with empty map.");
        }

        this.from = from;
        this.to = to;
        this.types = new HashSet<>();
        this.coordinates = new float[nodes.size() * 2];

        setRoadData(nodes);
    }

    private void setRoadData(List<Node> nodes) {
        // Get coordinates
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);

            coordinates[i * 2] = node.getX();
            coordinates[i * 2 + 1] = node.getY();
        }

        // Get traversal types
        String highway = tags.get("highway");
        if (highway != null) {
            types = Highway.getTraversalTypes(highway);
        } else {
            types = new HashSet<>();
        }

        if (tags.containsKey("motor_vehicle")) {
            if (tags.get("motor_vehicle").equals("no")) {
                types.remove("car");
            } else {
                types.add("car");
            }
        }

        if (tags.containsKey("bicycle")) {
            if (tags.get("bicycle").equals("no")) {
                types.remove("bicycle");
            } else {
                types.add("bicycle");
            }
        } else if (tags.containsKey("cycleway")) {
            types.add("bicycle");
        }

        if (tags.containsKey("foot") || tags.containsKey("footway") || tags.containsKey("sidewalk")) {
                types.add("feet");
        }

        // Get distance
        for (int i = 2; i < coordinates.length; i += 2) {
            float x1 = coordinates[i - 2];
            float y1 = coordinates[i - 1];
            float x2 = coordinates[i];
            float y2 = coordinates[i + 1];

            distance += Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        }

        //Get speed
        String maxspeed = tags.get("maxspeed");
        if (maxspeed == null) {
            if (highway == null) {
                speed = 50;
            } else {
                speed = Highway.getSpeed(highway);
            }
        } else {
            speed = Integer.parseInt(maxspeed);
        }

        // Get weight
        if (speed == 0) {
            weight = Double.POSITIVE_INFINITY;
        } else {
            weight = distance / speed;
        }

        //Find bounds
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < coordinates.length; i += 2) {
            double x = coordinates[i];
            double y = coordinates[i + 1];

            if (x < minX) {
                minX = x;
            }

            if (x > maxX) {
                maxX = x;
            }

            if (y < minY) {
                minY = y;
            }

            if (y > maxY) {
                maxY = y;
            }
        }

        boundingBox = new Bounds(minX, maxX, minY, maxY);
    }

    @Override
    public int getFrom() {
        return from;
    }

    @Override
    public int getTo() {
        return to;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public double getWeight(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Calling getWeight(String type) with null string.");
        }

        if (type.length() == 0) {
            throw new IllegalArgumentException("Calling getWeight(String type) with empty string.");
        }

        if (!types.contains(type)) {
            return Double.POSITIVE_INFINITY;
        }

        switch (type) {
            case "car" -> {
                return weight;
            }
            case "bicycle", "feet" -> {
                return distance;
            }
        }

        throw new IllegalArgumentException("Calling getWeight(String type) with invalid string.");
    }

    @Override
    public void draw(GraphicsContext graphicsContext) {
        if (graphicsContext == null) {
            throw new IllegalArgumentException("Calling draw(GraphicsContext graphicsContext) with null graphics context.");
        }

        graphicsContext.beginPath();
        graphicsContext.moveTo(coordinates[0], coordinates[1]);
        for (int i = 2 ; i < coordinates.length ; i += 2) {
            graphicsContext.lineTo(coordinates[i], coordinates[i + 1]);
        }

        graphicsContext.stroke();
    }

    @Override
    public Bounds getBoundingBox() {
        return boundingBox;
    }
}