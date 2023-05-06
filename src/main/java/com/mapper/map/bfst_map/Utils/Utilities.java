package com.mapper.map.bfst_map.Utils;

import com.mapper.map.bfst_map.Model.Elements.Node;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;

import java.util.*;

public class Utilities {

    //Math
    public static double nodeDistance(Node first, Node second) {
        return Math.abs(((Math.pow(first.getX(), 2.0) - Math.pow(second.getX(), 2.0)) - (Math.pow(first.getY(), 2.0) - Math.pow(second.getY(), 2.0))));
    }

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.abs(((Math.pow(x1, 2.0) - Math.pow(x2, 2.0)) - (Math.pow(y1, 2.0) - Math.pow(y2, 2.0))));
    }

    public static double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double calculation = Math.pow(Math.sin(latDistance / 2), 2) + Math.pow(Math.sin(lonDistance / 2), 2) * Math.cos(lat1) * Math.cos(lat2);

        double r = 6371;
        double distance = 2 * Math.asin(Math.sqrt(calculation));

        return r * distance;
    }

    public float[] calcCenter(float[] coords) {
        assert coords.length % 2 != 0 : "coords do not contain matching x-y pairs";

        int points = coords.length / 2;

        //coords length is two(Point)
        if (points == 1) {
            return coords;
            //coords length is four(Line)
        } else if (points == 2) {
            return calcCenterLine(coords);
            //coords length is six or above(Polygon)
        } else if (points >= 3) {
            return calcCenterPoly(coords);
        } else {
            throw new ArithmeticException("Center calculation dispatch failed. Point count error?");
        }
    }

    private float[] calcCenterPoly(float[] coords) {
        float xMin = Float.MAX_VALUE, xMax = Float.MIN_VALUE;
        float yMin = Float.MAX_VALUE, yMax = Float.MIN_VALUE;
        int arrayLen = coords.length;;

        for (int i = 0; i < arrayLen - 1; i++) {
            if (i % 2 == 0) {
                if (coords[i] > xMax) {
                    xMax = coords[i];
                } else if (coords[i] < xMin) {
                    xMin = coords[i];
                }
            } else {
                if (coords[i] > yMax) {
                    yMax = coords[i];
                } else if (coords[i] < yMin) {
                    yMin = coords[i];
                }
            }
        }

        float xMean = (xMin + xMax) * 0.5F;
        float yMean = (yMin + yMax) * 0.5F;


        return new float[]{xMean, yMean};
    }

    private float[] calcCenterLine(float[] coords) {
        float x1 = coords[0], x2 = coords[2];
        float y1 = coords[1], y2 = coords[3];

        return new float[]{(x1 + x2) / 2, (y1 + y2) / 2};
    }

    //Generic getters(to be removed later, implemented in Waypoint class)
    public static String getCountry(Waypoint waypoint) {
        if (waypoint.containsTag("addr:country")) {
            return waypoint.getTags().get("addr:country");
        } else {
            throw new NoSuchElementException("Given element has no assigned country");
        }
    }

    public static String getCity(Waypoint waypoint) {
        if (waypoint.containsTag("addr:city")) {
            return waypoint.getTags().get("addr:city");
        } else {
            throw new NoSuchElementException("Given element has no assigned city");
        }
    }

    public static String getStreet(Waypoint waypoint) {
        if (waypoint.containsTag("addr:street")) {
            return waypoint.getTags().get("addr:street");
        } else {
            throw new NoSuchElementException("Given element has no assigned street");
        }
    }

    public static String getHousenumber(Waypoint waypoint) {
        if (waypoint.containsTag("addr:housenumber")) {
            return waypoint.getTags().get("addr:housenumber");
        } else {
            throw new NoSuchElementException("Given element has no assigned housenumber");
        }
    }

    public static String getPostcode(Waypoint waypoint) {
        if (waypoint.containsTag("addr:postcode")) {
            return waypoint.getTags().get("addr:postcode");
        } else {
            throw new NoSuchElementException("Given element has no assigned postcode");
        }
    }
}