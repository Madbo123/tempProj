package com.mapper.map.bfst_map.Controller.GUI;

import com.mapper.map.bfst_map.Model.Elements.Model;
import com.mapper.map.bfst_map.Model.Elements.Way;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.Elements.Waypoint;
import com.mapper.map.bfst_map.Utils.Utilities;
import javafx.fxml.FXML;
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
            mapController.removeFocus();
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
        int drawOffset = 200;
        Point2D upperLeftCorner = mouseToModel(drawOffset, drawOffset);
        Point2D upperRightCorner = mouseToModel(mapCanvas.getWidth() - drawOffset, drawOffset);
        Point2D lowerRightCorner = mouseToModel(mapCanvas.getWidth() - drawOffset, mapCanvas.getHeight() - drawOffset);
        Point2D lowerLeftCorner = mouseToModel(drawOffset, mapCanvas.getHeight() - drawOffset);
        Bounds searchBounds = new Bounds((float) lowerLeftCorner.getX(), (float) upperRightCorner.getX(), (float) upperLeftCorner.getY(), (float) lowerRightCorner.getY());

        Model.rTreeController.draw(gc, trans, searchBounds, trans.determinant());

        // Draw path
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3/Math.sqrt(trans.determinant()));
        Model.dijkstraController.drawPath(gc);

        double x = searchBounds.getMinX();
        double y = searchBounds.getMinY();
        double w = searchBounds.getMaxX() - searchBounds.getMinX();
        double h = searchBounds.getMaxY() - searchBounds.getMinY();
        gc.setStroke(Color.RED);
        gc.strokeRect(x, y, w, h);

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
                    closestElement = way;
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