package com.mapper.map.bfst_map.Controller.RTree;

import com.mapper.map.bfst_map.Model.Dijkstra.Road;
import com.mapper.map.bfst_map.Model.Elements.Relation;
import com.mapper.map.bfst_map.Model.Elements.Way;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import com.mapper.map.bfst_map.Model.RTree.RTree;
import com.mapper.map.bfst_map.Utils.Highway;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import java.util.*;


public class RTreeControllerOld {
    private final RTree motorwayRTree;
    private final RTree trunkRTree;
    private final RTree primaryRTree;
    private final RTree secondaryRTree;
    private final RTree tertiaryRTree;
    private final RTree highwayRTree;
    private final RTree landuseRTree;
    private final RTree naturalRTree;
    private final RTree buildingRTree;
    private final RTree waterwayRTree;
    private final RTree foundationRTree;

    public RTreeControllerOld(List<Way> ways, List<Relation> relations, List<Road> roads) {

        List<HasBoundingBox> motorwayList = new ArrayList<>();
        List<HasBoundingBox> trunkList = new ArrayList<>();
        List<HasBoundingBox> primaryList = new ArrayList<>();
        List<HasBoundingBox> secondaryList = new ArrayList<>();
        List<HasBoundingBox> tertiaryList = new ArrayList<>();
        List<HasBoundingBox> highwayList = new ArrayList<>();
        List<HasBoundingBox> landuseList = new ArrayList<>();
        List<HasBoundingBox> naturalList = new ArrayList<>();
        List<HasBoundingBox> buildingList = new ArrayList<>();
        List<HasBoundingBox> waterwayList = new ArrayList<>();
        List<HasBoundingBox> foundationList = new ArrayList<>();






        for (Relation relation : relations) {
            Map<String, String> tags = relation.getTags();

            if (tags.containsKey("landuse")) {
                landuseList.add(relation);
            } else if (tags.containsKey("building")) {
                buildingList.add(relation);
            } else if (tags.containsKey("natural")) {
                naturalList.add(relation);
            } else if (tags.containsKey("waterway")) {
                waterwayList.add(relation);
            } else {
                foundationList.add(relation);
            }
        }

        for (Way way : ways) {
            Map<String, String> tags = way.getTags();

            if (tags.containsKey("landuse")) {
                landuseList.add(way);
            } else if (tags.containsKey("building")) {
                buildingList.add(way);
            } else if (tags.containsKey("natural")) {
                naturalList.add(way);
            } else if (tags.containsKey("waterway")) {
                waterwayList.add(way);
            } else {
                foundationList.add(way);
            }
        }

        for (Road road : roads) {
            Map<String, String> tags = road.getTags();

            if (tags.containsKey("highway")) {
                String highway = tags.get("highway");

                switch (highway) {
                    case "motorway" -> motorwayList.add(road);
                    case "trunk" -> trunkList.add(road);
                    case "primary" -> primaryList.add(road);
                    case "secondary" -> secondaryList.add(road);
                    case "tertiary" -> tertiaryList.add(road);
                    default -> highwayList.add(road);
                }
            }
        }

        motorwayRTree = loadRTree(motorwayList);
        trunkRTree = loadRTree(trunkList);
        primaryRTree = loadRTree(primaryList);
        secondaryRTree = loadRTree(secondaryList);
        tertiaryRTree = loadRTree(tertiaryList);
        highwayRTree = loadRTree(highwayList);
        landuseRTree = loadRTree(landuseList);
        naturalRTree = loadRTree(naturalList);
        buildingRTree = loadRTree(buildingList);
        waterwayRTree = loadRTree(waterwayList);
        foundationRTree = loadRTree(foundationList);

    }

    private RTree loadRTree(List<HasBoundingBox> elements) {
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;

        for (HasBoundingBox element : elements) {
            Bounds bounds = element.getBoundingBox();

            double wayMinX = bounds.getMinX();
            double wayMaxX = bounds.getMaxX();
            double wayMinY = bounds.getMinY();
            double wayMaxY = bounds.getMaxY();

            minX = Math.min(minX, wayMinX);
            minY = Math.min(minY, wayMinY);

            maxX = Math.max(maxX, wayMaxX);
            maxY = Math.max(maxY, wayMaxY);

            //Tænker ovenstående er lidt nemmere at læse? Ellers ændres det bare tilbage.

            /* if (wayMinX < minX) {
                minX = wayMinX;
            }

            if (wayMaxX > maxX) {
                maxX = wayMaxX;
            }

            if (wayMinY < minY) {
                minY = wayMinY;
            }

            if (wayMaxY > maxY) {
                maxY = wayMaxY;
            } */
        }

        Bounds bounds = new Bounds(minX, maxX, minY, maxY);
        RTree rTree = new RTree(elements, true, bounds);
        rTree.validate();

        return rTree;
    }

    public void draw(GraphicsContext graphicsContext, Affine affine, Bounds bounds, double zoomLevel) {


        List<HasBoundingBox> motorwayList = motorwayRTree.boundsSearch(bounds);
        List<HasBoundingBox> trunkList = trunkRTree.boundsSearch(bounds);
        List<HasBoundingBox> primaryList = primaryRTree.boundsSearch(bounds);
        List<HasBoundingBox> secondaryList = secondaryRTree.boundsSearch(bounds);
        List<HasBoundingBox> tertiaryList = tertiaryRTree.boundsSearch(bounds);
        List<HasBoundingBox> highwayList = highwayRTree.boundsSearch(bounds);
        List<HasBoundingBox> landuseList = landuseRTree.boundsSearch(bounds);
        List<HasBoundingBox> naturalList = naturalRTree.boundsSearch(bounds);
        List<HasBoundingBox> buildingList = buildingRTree.boundsSearch(bounds);
        List<HasBoundingBox> waterwayList = waterwayRTree.boundsSearch(bounds);
        List<HasBoundingBox> foundationList = foundationRTree.boundsSearch(bounds);

        List<HasBoundingBox> elements = new ArrayList<>();
        elements.addAll(foundationList);
        elements.addAll(waterwayList);
        elements.addAll(landuseList);
        elements.addAll(naturalList);
        elements.addAll(buildingList);
        elements.addAll(highwayList);


        graphicsContext.setStroke(Color.BEIGE);
        graphicsContext.setLineWidth(1 / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : elements) {
            element.draw(graphicsContext);
        }

        graphicsContext.setStroke(Highway.getColor("tertiary"));
        graphicsContext.setLineWidth(Highway.getSize("tertiary") / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : tertiaryList) {
            element.draw(graphicsContext);
        }

        graphicsContext.setStroke(Highway.getColor("secondary"));
        graphicsContext.setLineWidth(Highway.getSize("secondary") / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : secondaryList) {
            element.draw(graphicsContext);
        }

        graphicsContext.setStroke(Highway.getColor("primary"));
        graphicsContext.setLineWidth(Highway.getSize("primary") / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : primaryList) {
            element.draw(graphicsContext);
        }

        graphicsContext.setStroke(Highway.getColor("trunk"));
        graphicsContext.setLineWidth(Highway.getSize("trunk") / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : trunkList) {
            element.draw(graphicsContext);
        }

        graphicsContext.setStroke(Highway.getColor("motorway"));
        graphicsContext.setLineWidth(Highway.getSize("motorway") / Math.sqrt(affine.determinant()));
        for (HasBoundingBox element : motorwayList) {
            element.draw(graphicsContext);
        }
    }
}