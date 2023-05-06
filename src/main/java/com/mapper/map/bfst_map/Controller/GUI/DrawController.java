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
        colorMap.put("cable_car", Color.rgb(22, 22, 21));
        colorMap.put("gondola", Color.rgb(22, 22, 21));
        colorMap.put("mixed_lift", Color.rgb(22, 22, 21));
        colorMap.put("chair_lift", Color.rgb(22, 22, 21));
        colorMap.put("drag_lift", Color.rgb(22, 22, 21));
        colorMap.put("t-bar", Color.rgb(22, 22, 21));
        colorMap.put("j-bar", Color.rgb(22, 22, 21));
        colorMap.put("platter", Color.rgb(22, 22, 21));
        colorMap.put("rope_tow", Color.rgb(22, 22, 21));
        colorMap.put("zip_line", Color.rgb(22, 22, 21));
        colorMap.put("goods", Color.rgb(22, 22, 21));
        colorMap.put("station", Color.rgb(176, 158, 143));
        colorMap.put("apron", Color.rgb(219, 219, 225));
        colorMap.put("runway", Color.rgb(187, 187, 204));
        colorMap.put("taxiway", Color.rgb(187, 187, 204));
        colorMap.put("terminal", Color.rgb(197, 184, 173));
        colorMap.put("grave_yard", Color.rgb(172, 204, 177));
        colorMap.put("city_wall", Color.rgb(98, 99, 99));
        colorMap.put("fence", Color.rgb(98, 99, 99));
        colorMap.put("guard_rail", Color.rgb(98, 99, 99));
        colorMap.put("hedge", Color.rgb(170, 207, 157));
        colorMap.put("handrail", Color.rgb(98, 99, 99));
        colorMap.put("retaining_wall", Color.rgb(98, 99, 99));
        colorMap.put("wall", Color.rgb(98, 99, 99));
        colorMap.put("chain", Color.rgb(98, 99, 99));
        colorMap.put("citywalls", Color.rgb(98, 99, 99));
        colorMap.put("commercial", Color.rgb(238, 208, 207));
        colorMap.put("construction", Color.rgb(199, 199, 181));
        colorMap.put("industrial", Color.rgb(230, 209, 226));
        colorMap.put("residential", Color.rgb(218, 218, 218));
        colorMap.put("retail", Color.rgb(254, 203, 198));
        colorMap.put("allotments", Color.rgb(202, 225, 194));
        colorMap.put("farmland", Color.rgb(238, 240, 215));
        colorMap.put("farmyard", Color.rgb(234, 205, 167));
        colorMap.put("forest", Color.rgb(157, 201, 141));
        colorMap.put("greenhouse_horticulture", Color.rgb(238, 240, 215));
        colorMap.put("meadow", Color.rgb(206, 235, 180));
        colorMap.put("orchard", Color.rgb(158, 219, 147));
        colorMap.put("plant_nursery", Color.rgb(177, 224, 168));
        colorMap.put("vineyard", Color.rgb(158, 219, 147));
        colorMap.put("basin", Color.rgb(172, 211, 223));
        colorMap.put("reservoir", Color.rgb(172, 211, 223));
        colorMap.put("salt_pond", Color.rgb(172, 211, 223));
        colorMap.put("brownfield", Color.rgb(199, 199, 181));
        colorMap.put("cemetery", Color.rgb(172, 204, 177));
        colorMap.put("garages", Color.rgb(222, 221, 205));
        colorMap.put("grass", Color.rgb(206, 235, 180));
        colorMap.put("greenfield", Color.rgb(241, 238, 232));
        colorMap.put("landfill", Color.rgb(182, 182, 146));
        colorMap.put("military", Color.rgb(243, 228, 222));
        colorMap.put("quarry", Color.rgb(183, 181, 181));
        colorMap.put("railway", Color.rgb(230, 209, 226));
        colorMap.put("recreation_ground", Color.rgb(224, 252, 228));
        colorMap.put("religious", Color.rgb(206, 205, 202));
        colorMap.put("village_green", Color.rgb(206, 235, 180));
        colorMap.put("garden", Color.rgb(207, 235, 180));
        colorMap.put("park", Color.rgb(206, 247, 204));
        colorMap.put("pitch", Color.rgb(172, 224, 205));
        colorMap.put("swimming_pool", Color.rgb(172, 211, 223));
        colorMap.put("beacon", Color.rgb(184, 184, 184));
        colorMap.put("bridge", Color.rgb(184, 184, 184));
        colorMap.put("cutline", Color.rgb(197, 237, 182));
        colorMap.put("embankment", Color.rgb(22, 22, 21));
        colorMap.put("goods_conveyor", Color.rgb(22, 22, 21));
        colorMap.put("groyne", Color.rgb(184, 184, 184));
        colorMap.put("pier", Color.rgb(243, 240, 234));
        colorMap.put("wastewater_plant", Color.rgb(235, 219, 232));
        colorMap.put("water_works", Color.rgb(235, 219, 232));
        colorMap.put("works", Color.rgb(242, 239, 233));
        colorMap.put("danger_area", Color.rgb(243, 224, 220));
        colorMap.put("grassland", Color.rgb(206, 235, 180));
        colorMap.put("heath", Color.rgb(215, 218, 164));
        colorMap.put("scrub", Color.rgb(201, 216, 175));
        colorMap.put("tree", Color.rgb(172, 209, 158));
        colorMap.put("tree_row", Color.rgb(172, 209, 158));
        colorMap.put("wood", Color.rgb(172, 209, 158));
        colorMap.put("bay", Color.rgb(172, 211, 223));
        colorMap.put("beach", Color.rgb(255, 241, 189));
        colorMap.put("coastline", Color.rgb(241, 238, 232));
        colorMap.put("reef", Color.rgb(172, 211, 223));
        colorMap.put("shingle", Color.rgb(237, 229, 221));
        colorMap.put("shoal", Color.rgb(255, 242, 191));
        colorMap.put("strait", Color.rgb(172, 211, 223));
        colorMap.put("water", Color.rgb(172, 211, 223));
        colorMap.put("wetland", Color.rgb(136, 189, 240));
        colorMap.put("arete", Color.rgb(22, 22, 21));
        colorMap.put("bare_rock", Color.rgb(227, 221, 214));
        colorMap.put("cliff", Color.rgb(22, 22, 21));
        colorMap.put("ridge", Color.rgb(22, 22, 21));
        colorMap.put("sand", Color.rgb(245, 233, 200));
        colorMap.put("scree", Color.rgb(227, 221, 214));
        colorMap.put("generator", Color.rgb(227, 205, 222));
        colorMap.put("line", Color.rgb(22, 22, 21));
        colorMap.put("minor_line", Color.rgb(22, 22, 21));
        colorMap.put("plant", Color.rgb(227, 205, 222));
        colorMap.put("substation", Color.rgb(227, 205, 222));
        colorMap.put("disused", Color.rgb(22, 22, 21));
        colorMap.put("funicular", Color.rgb(22, 22, 21));
        colorMap.put("light_rail", Color.rgb(22, 22, 21));
        colorMap.put("miniature", Color.rgb(22, 22, 21));
        colorMap.put("monorail", Color.rgb(22, 22, 21));
        colorMap.put("narrow_gauge", Color.rgb(22, 22, 21));
        colorMap.put("preserved", Color.rgb(22, 22, 21));
        colorMap.put("rail", Color.rgb(22, 22, 21));
        colorMap.put("subway", Color.rgb(22, 22, 21));
        colorMap.put("tram", Color.rgb(22, 22, 21));
        colorMap.put("siding", Color.rgb(22, 22, 21));
        colorMap.put("spur", Color.rgb(22, 22, 21));
        colorMap.put("yard", Color.rgb(22, 22, 21));
        colorMap.put("turntable", Color.rgb(22, 22, 21));
        colorMap.put("ferry", Color.rgb(143, 166, 235));
        colorMap.put("theme_park", Color.rgb(242, 240, 234));
        colorMap.put("river", Color.rgb(172, 211, 223));
        colorMap.put("riverbank", Color.rgb(172, 211, 223));
        colorMap.put("stream", Color.rgb(172, 211, 223));
        colorMap.put("canal", Color.rgb(172, 211, 223));
        colorMap.put("drain", Color.rgb(172, 211, 223));
        colorMap.put("ditch", Color.rgb(172, 211, 223));
        colorMap.put("dock", Color.rgb(172, 211, 223));
        colorMap.put("building", Color.rgb(192, 182, 182));

/*
        colorMap.put("island", Color.rgb(208, 213, 208));
        colorMap.put("island", Color.rgb(208, 213, 208));
        colorMap.put("building", Color.rgb(192, 182, 182));
        colorMap.put("amenity_lightyellow", Color.rgb(241, 255, 218));
        colorMap.put("pedestrian", Color.rgb(133, 150, 150));
        colorMap.put("sports", Color.rgb(96, 185, 120));
        colorMap.put("forest_green", Color.rgb(66, 126, 66));
        colorMap.put("nature_green", Color.rgb(188, 255, 160));
        colorMap.put("landuse_general", Color.rgb(164, 164, 164));
        colorMap.put("landuse_construction", Color.rgb(154, 140, 140));
        colorMap.put("bridge_general", Color.rgb(126, 126, 126));
 */
    }


    public static void fillElement(Way way, GraphicsContext gc) {
        if (!way.getAssignedColor().equals("unassigned") && way.getAssignedColor() != null) {
            gc.setFill(colorMap.get(way.getAssignedColor()));
            gc.fill();
        }
    }

    public static void assignColor(Way way) {
        for (String tag : colorMap.keySet()) {
            if (way.containsTag(tag)) {
                way.assignColor(tag);
                return;
            }
        }

        /*
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
*/
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

}