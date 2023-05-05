package com.mapper.map.bfst_map.Controller.GUI;

import com.mapper.map.bfst_map.Model.Elements.Way;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;
import com.mapper.map.bfst_map.Utils.Highway;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawController {

    protected static final HashMap<String, Color> colorMap = new HashMap<>();
    protected static GraphicsContext gc;


    public static void initColorMap() {

        colorMap.put("island", Color.rgb(208, 213, 208));
        colorMap.put("building", Color.rgb(192, 182, 182));
        colorMap.put("amenity_lightyellow", Color.rgb(241, 255, 218));
        colorMap.put("water", Color.rgb(104, 175, 253));
        colorMap.put("pedestrian", Color.rgb(133, 150, 150));
        colorMap.put("sports", Color.rgb(96, 185, 120));
        colorMap.put("forest_green", Color.rgb(66, 126, 66));
        colorMap.put("nature_green", Color.rgb(188, 255, 160));
        colorMap.put("landuse_general", Color.rgb(164, 164, 164));
        colorMap.put("landuse_construction", Color.rgb(154, 140, 140));
        colorMap.put("bridge_general", Color.rgb(126, 126, 126));
    }


    public static void fillElement(Way way, GraphicsContext gc) {
        if (!way.getAssignedColor().equals("unassigned") && way.getAssignedColor() != null) {
            gc.setFill(colorMap.get(way.getAssignedColor()));
            gc.fill();
        }
    }

    public static void assignColor(Way way) {
        if (way.containsTag("island") | way.containsTag("islet")) {
            way.assignColor("island");
            return;
        }

        if (way.containsTag("building") | way.containsTag("parking") | way.containsTag("bicycle_parking") | way.containsTag("chapel")) {
            way.assignColor("building");
            return;
        }

        if (way.containsTag("amenity") && (way.containsTag("hospital") | way.containsTag("school") | way.containsTag("kindergarten") | way.containsTag("university"))) {
            way.assignColor("amenity_lightyellow");
            return;
        }

        if (way.containsTag("marina") | way.containsTag("water")) {
            way.assignColor("water");
            return;
        }

        if (way.containsTag("pedestrian")) {
            way.assignColor("pedestrian");
            return;
        }

        if (way.containsTag("pitch")) {
            way.assignColor("sports");
            return;
        }

        if (way.containsTag("forest") | way.containsTag("scrub")) {
            way.assignColor("forest_green");
            return;
        }

        if (way.containsTag("grass") | way.containsTag("meadow") | way.containsTag("recreation_ground") | way.containsTag("cemetery") | way.containsTag("allotments") | way.containsTag("park") | way.containsTag("garden")) {
            way.assignColor("nature_green");
            return;
        }

        if (way.containsTag("landuse")) {
            if (way.containsTag("residential") | way.containsTag("basin") | way.containsTag("industrial")) {
                way.assignColor("landuse_general");
                return;
            } else if (way.containsTag("construction") | way.containsTag("brownfield")) {
                way.assignColor("landuse_construction");
                return;
            }
            return;
        }

        if (way.containsTag("bridge") && !way.containsTag("subway") && !way.containsTag("footway")) {
            way.assignColor("bridge_general");
        }

    }
    public static void assignType(Way way) {

    }



    public static void reassignColorMapValue(String colorKey, Color colorValue) {
        if (colorMap.containsKey(colorKey)) {
            colorMap.put(colorKey, colorValue);
        } else {
            throw new IllegalArgumentException("Can only reassign colors to pre-existing tags");
        }
    }



    public static void toggleColorTheme(Boolean isDarkMode, AnchorPane mapCanvasAnchor) {
        List<String> tempKeyList = new ArrayList<>(colorMap.keySet());

        if (isDarkMode) {
            mapCanvasAnchor.setStyle("-fx-background-color:  #D0D5D0FF");
            for (String key : tempKeyList) {
                colorMap.put(key, colorMap.get(key).brighter());
            }
        } else {
            mapCanvasAnchor.setStyle("-fx-background-color: #595959;");
            for (String key : tempKeyList) {
                colorMap.put(key, colorMap.get(key).darker());
            }
        }
    }


    public static void updateGC(Affine affine, String type) {
        if (Highway.getColor(type) != null && Highway.getSize(type) != 0) {
            gc.setStroke(Highway.getColor(type));
            gc.setLineWidth(Highway.getSize(type) / Math.sqrt(affine.determinant()));
        } else {
            gc.setStroke(Color.BEIGE);
            gc.setLineWidth(1 / Math.sqrt(affine.determinant()));
        }
    }





    public static void addMultiple(String string, Color color) {
        for (String strings : string.split(", ")) {
            colorMap.put(strings, color);
        }
    }



    //Uddateret, bruges nu kun for at teste eller lignende - MB
    public static void fillElement2(Way way, GraphicsContext gc) {

            if (way.containsTag("island") | way.containsTag("islet")) {
                gc.setFill(Color.rgb(208, 213, 208));
                gc.fill();
            } else if(way.containsTag("building") | way.containsTag("parking") | way.containsTag("bicycle_parking") | way.containsTag("chapel")) {
                gc.setFill(Color.rgb(192, 182, 182));
                gc.fill();
            } else if (way.containsTag("amenity") && (way.containsTag("hospital") | way.containsTag("school") | way.containsTag("kindergarten") | way.containsTag("university"))) {
                gc.setFill(Color.rgb(241, 255, 218));
                gc.fill();
            } else if(way.containsTag("marina") | way.containsTag("water")) {
                gc.setFill(Color.rgb(104, 175, 253));
                gc.fill();
            } else if (way.containsTag("pedestrian")) {
                gc.setFill(Color.rgb(133, 150, 150));
                gc.fill();
            } else if (way.containsTag("pitch")) {
                gc.setFill(Color.rgb(96, 185, 120));
                gc.fill();
            } else if (way.containsTag("forest") | way.containsTag("scrub")) {
                gc.setFill(Color.rgb(66, 126, 66));
                gc.fill();
            } else if (way.containsTag("grass") | way.containsTag("meadow") | way.containsTag("recreation_ground") | way.containsTag("cemetery") | way.containsTag("allotments") | way.containsTag("park") | way.containsTag("garden")) {
                gc.setFill(Color.rgb(188, 255, 160));
                gc.fill();
            } else if (way.containsTag("landuse")) {
                if (way.containsTag("residential") | way.containsTag("basin") | way.containsTag("industrial")) {
                    gc.setFill(Color.rgb(164, 164, 164));
                    gc.fill();
                } else if (way.containsTag("construction") | way.containsTag("brownfield")) {
                    gc.setFill(Color.rgb(154, 140, 140));
                    gc.fill();
                }
            } else if (way.containsTag("bridge") && !way.containsTag("subway") && !way.containsTag("footway")) {
                gc.setFill(Color.rgb(126, 126, 126));
                gc.fill();
            }
        }

    public static void drawInOrder() {

    }

}
