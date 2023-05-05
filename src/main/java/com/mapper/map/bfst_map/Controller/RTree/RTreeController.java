package com.mapper.map.bfst_map.Controller.RTree;

import com.mapper.map.bfst_map.Controller.GUI.DrawController;
import com.mapper.map.bfst_map.Model.Dijkstra.Road;
import com.mapper.map.bfst_map.Model.Elements.Relation;
import com.mapper.map.bfst_map.Model.Elements.Way;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import com.mapper.map.bfst_map.Model.RTree.RTree;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

import java.util.*;

public class RTreeController {

    private HashMap<String, List<HasBoundingBox>> typeListMap = new HashMap<>();
    private HashMap<String, RTree> typeTreeMap = new HashMap<>();

    public RTreeController(List<Way> ways, List<Relation> relations, List<Road> roads) {

        //Helper method til at putte keywords in, dette kan forbedres ved at binde "keytypes" til objektet i sig selv.
        putMultiple(typeListMap, "motorway, trunk, primary, secondary, tertiary, highway, landuse, natural, building, waterway, foundation");


        //Stadig kodeduplikering her. Fikses ved at binde typer der bestemmer drawing order med objekter i en global map eller lign.


        for (Relation relation : relations) {
            sorterThingie(relation);
        }

        for (Way way : ways) {
            sorterThingie(way);
        }

        for (Road road : roads) {
            Map<String, String> tags = road.getTags();

            if (tags.containsKey("highway")) {
                String temp = tags.get("highway");
                switch (temp) {
                    case "motorway", "trunk", "primary", "secondary", "tertiary" -> typeListMap.get(temp).add(road);
                    default -> typeListMap.get("highway").add(road);
                }
            }
        }

        //Helper method, kan ses i bunden.
        TTMapPut(typeListMap);
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
        }

        Bounds bounds = new Bounds(minX, maxX, minY, maxY);
        RTree rTree = new RTree(elements, true, bounds);
        rTree.validate();

        return rTree;
    }

    //Draw burde nok omskrives til ikke at behøve gc som param.
    public void draw(GraphicsContext graphicsContext, Affine affine, Bounds bounds, double zoomLevel) {

        //Lister der kan itereres igennem for drawing order. Dette flyttes til draw controller soonTM
        List<String> elementsOrder = List.of("foundation", "waterway", "landuse", "natural", "building", "highway");
        List<String> roadOrder = List.of("tertiary", "secondary", "primary", "trunk", "motorway");


        //Opdaterer lister
        for (String key : typeTreeMap.keySet()) {
            typeListMap.put(key, typeTreeMap.get(key).boundsSearch(bounds));
        }


        //Opdaterer GC udenfor loopet da samme indstillinger bruges for alle de generiske elementer, men dette ville nok ikke betyde det store, selvom det var i loopet
        DrawController.updateGC(affine, "element");

        //Nested loop, ja, men første loop gennemgår kun string types, så det er ikke specielt intensivt.
        for (String type : elementsOrder) {
            for (HasBoundingBox element : typeListMap.get(type)) {
                element.draw(graphicsContext);
            }
        }


        //Same here
        for (String type : roadOrder) {
            DrawController.updateGC(affine, type);
            for (HasBoundingBox road : typeListMap.get(type)) {
                road.draw(graphicsContext);
            }
        }
    }

    //Helper methods fordi jeg er doven
    private void sorterThingie(Waypoint waypoint) {
        if (waypoint.containsTag("landuse")) {
            typeListMap.get("landuse").add((HasBoundingBox) waypoint);
        } else if (waypoint.containsTag("building")) {
            typeListMap.get("building").add((HasBoundingBox) waypoint);
        } else if (waypoint.containsTag("natural")) {
            typeListMap.get("natural").add((HasBoundingBox) waypoint);
        } else if (waypoint.containsTag("waterway")) {
            typeListMap.get("waterway").add((HasBoundingBox) waypoint);
        } else {
            typeListMap.get("foundation").add((HasBoundingBox) waypoint);
        }
    }

    private void putMultiple(HashMap<String, List<HasBoundingBox>> map, String newTypes) {
        for (String type : newTypes.split(", ")) {
            map.put(type, new ArrayList<>());
        }
    }

    private void TTMapPut(HashMap<String, List<HasBoundingBox>> map) {
        for (String type : map.keySet()) {
            typeTreeMap.put(type, loadRTree(typeListMap.get(type)));
        }
    }
}