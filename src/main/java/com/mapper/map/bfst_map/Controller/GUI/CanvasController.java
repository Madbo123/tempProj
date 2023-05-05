package com.mapper.map.bfst_map.Controller.GUI;

import com.mapper.map.bfst_map.Model.Dijkstra.Road;
import com.mapper.map.bfst_map.Model.Elements.Model;
import com.mapper.map.bfst_map.Model.Elements.Relation;
import com.mapper.map.bfst_map.Model.Elements.Way;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import com.mapper.map.bfst_map.Utils.Highway;
import com.mapper.map.bfst_map.Utils.Utilities;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.util.ArrayList;
import java.util.List;

import static com.mapper.map.bfst_map.Controller.GUI.MapController.model;

public class CanvasController {

    //Controller for the canvas - MB

    @FXML
    private Canvas mapCanvas;
    double lastX, lastY;

    protected MapController mapController = null;

    GraphicsContext gc;

    Affine trans = new Affine();

    //See MapController for description of inits - MB
    public void initialize() {
        gc = mapCanvas.getGraphicsContext2D();
        DrawController.gc = gc;
        initActions();
        initView();
    }

    void initActions() {
        mapCanvas.setOnMousePressed(e -> {
            lastX = e.getX();
            lastY = e.getY();
        });

        mapCanvas.setOnMouseDragged(e -> {
            if (e.isPrimaryButtonDown()) {
                double dx = e.getX() - lastX;
                double dy = e.getY() - lastY;
                pan(dx, dy);
            } else if (e.isSecondaryButtonDown()) {
                Point2D lastmodel = mouseToModel(lastX, lastY);
                Point2D newmodel = mouseToModel(e.getX(), e.getY());
                model.add(lastmodel, newmodel);
                redraw();
            }

            lastX = e.getX();
            lastY = e.getY();
        });

        mapCanvas.setOnScroll(e -> {
            double factor = e.getDeltaY();
            zoom(e.getX(), e.getY(), Math.pow(1.01, factor));
        });
    }

    void initView() {
        trans.prependTranslation(-0.56*model.getMinLon(), model.getMaxLat());

        trans.prependTranslation(0, 0);
        trans.prependScale(mapCanvas.getHeight() / (model.getMaxLat() - model.getMinLat()), mapCanvas.getHeight() / (model.getMaxLat() - model.getMinLat()));
        trans.prependTranslation(0, 0);
    }

    public void redraw() {
        gc.setTransform(new Affine());
        gc.setStroke(Color.BLACK);

        gc.clearRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));

        // Draw R-tree
        Point2D upperLeftCorner = mouseToModel(0, 0);
        Point2D upperRightCorner = mouseToModel(mapCanvas.getWidth(), 0);
        Point2D lowerRightCorner = mouseToModel(mapCanvas.getWidth(), mapCanvas.getHeight());
        Point2D lowerLeftCorner = mouseToModel(0, mapCanvas.getHeight());
        Bounds searchBounds = new Bounds(lowerLeftCorner.getX(), upperRightCorner.getX(), upperLeftCorner.getY(), lowerRightCorner.getY());
        Model.rTreeController.draw(gc, trans, searchBounds, 0.0);

        // Draw path
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3/Math.sqrt(trans.determinant()));
        Model.dijkstraController.drawPath(gc);

        //Burde IKKE gøres sådan her, det skal bare lige testes og er nemt således.

        /*
        List<Way> drawnWays = new ArrayList<>(model.getTaggedWays());



        for (Relation relation : model.getRelations()) {
            relation.draw(gc);
        }

        for (Way way : model.getWays()) {
            if (way.containsTag("landuse") | (way.containsTag("square") && way.containsTag("pedestrian"))) {
                way.draw(gc);
                drawnWays.add(way);
            }
        }


        for (Way way : model.getWays()) {
            if (way.containsTag("amenity")) {
                way.draw(gc);
                drawnWays.add(way);
            }
        }


        for (Way way : model.getWays()) {
            if (!drawnWays.contains(way) && !way.containsTag("highway")) {
                way.draw(gc);
                drawnWays.add(way);
            }
        }


        Model.dijkstraController.drawRoads(trans);

        gc.setStroke(Color.BEIGE);
        double bigHighway = 4/Math.sqrt(trans.determinant());
        double smallHighway = 2/Math.sqrt(trans.determinant());

        for (Way way : retrievedWays) {
            if (!drawnWays.contains(way)) {
                if (way.getTags().get("highway").equals("tertiary")) {
                    gc.setLineWidth(bigHighway);
                    way.draw(gc);
                    drawnWays.add(way);
                } else if (!way.containsTag("pedestrian")) {
                    gc.setLineWidth(smallHighway);
                    way.draw(gc);
                    drawnWays.add(way);
                }
            }
        }
         */
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        pan(dx, dy);
        redraw();
    }

    public void infoOnHover(MouseEvent e) {
        mapController.updateInfoLabel(e);
    }

    public Waypoint getClosest(MouseEvent e) {
        //Framework for getting the element closest to the cursor


        double x = e.getX(), y = e.getY();

        List<Way> RTreeList = new ArrayList<>();

        double closest = Double.MAX_VALUE;
        Waypoint closestElement = null;

        for (Way way : RTreeList) {
            for (int i = 0; i < way.coords.length / 2; i = i + 2) {
                double pointDistance = Utilities.distance(x, way.coords[i], y, way.coords[i + 1]);
                if (pointDistance < closest) {
                    closest = pointDistance;
                    //closestElement = way;
                }
            }
        }

        return closestElement;
    }

    public Point2D mouseToModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

    }
}