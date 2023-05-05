package com.mapper.map.bfst_map.Model.Elements;

import com.mapper.map.bfst_map.Controller.GUI.DrawController;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mapper.map.bfst_map.Controller.GUI.DrawController.fillElement;

public class Way extends Waypoint implements Serializable, HasBoundingBox {
    public float[] coords;

    long way_id;

    private String assignedColor = "unassigned";
    private ArrayList<Node> wayNodes;


    public int prio = 1;


    Bounds boundingBox;


    public Way(ArrayList<Node> wayNodes, HashMap<String, String> tags) {
        super(tags);
        this.wayNodes = new ArrayList<>(wayNodes);





        coords = new float[wayNodes.size() * 2];

        for (int i = 0 ; i < wayNodes.size() ; ++i) {
            Node node = wayNodes.get(i);
            coords[2 * i] = node.getX();
            coords[2 * i + 1] = node.getY();
        }

        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < coords.length; i += 2) {
            double x = coords[i];
            double y = coords[i + 1];

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

        DrawController.assignColor(this);
    }


    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }

        fillElement(this, gc);
    }




    public void assignID(long way_id) {
        this.way_id = way_id;
    }

    public void assignColor(String colorName) {
        if (colorName != null) {
            assignedColor = colorName;
        } else {
            throw new IllegalArgumentException("Tried to assign color as 'Null' :(");
        }
    }

    public void assignTags(HashMap<String, String> tags) {
        this.tags = tags;
    }




    public ArrayList<Node> getWayNodes() {
        return wayNodes;
    }

    public void clearWayNodes() {
        wayNodes.clear();
    }

    public Node getFirstNode() {
        return wayNodes.get(0);
    }

    public Node getLastNode() {
        return wayNodes.get(wayNodes.size() - 1);
    }

    public String getAssignedColor() {
        return assignedColor;
    }


    @Override
    public Bounds getBoundingBox() {
        return boundingBox;
    }
}
