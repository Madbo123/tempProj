package com.mapper.map.bfst_map.Model.RTree;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RTree implements HasBoundingBox {
    private final Bounds boundingBox;
    private List<HasBoundingBox> leafElements;
    private RTree leftBranch;
    private RTree rightBranch;

    public RTree(List<HasBoundingBox> mapElements, boolean sortByX, Bounds parentBounds) {
        boundingBox = new Bounds(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);

        if(mapElements.size() > 5){
            List<HasBoundingBox> branch;
            if(sortByX){
                branch = sortByX(mapElements);
                boundingBox.setMinY(parentBounds.getMinY());
                boundingBox.setMaxY(parentBounds.getMaxY());
                boundingBox.setMinX(mapElements.get(0).getBoundingBox().getMinX()); //the minX of the first element in the sorted list must be the minX overall
                boundingBox.setMaxX(mapElements.get(mapElements.size() - 1).getBoundingBox().getMaxX()); //same logic as above, just with the last element and maxX
            } else{
                branch = sortByY(mapElements);
                boundingBox.setMinX(parentBounds.getMinX());
                boundingBox.setMaxX(parentBounds.getMaxX());
                boundingBox.setMinY(mapElements.get(0).getBoundingBox().getMinY()); //the minX of the first element in the sorted list must be the minX overall
                boundingBox.setMaxY(mapElements.get(mapElements.size() - 1).getBoundingBox().getMaxY());

            }

            leftBranch = new RTree(branch.subList(0, (branch.size() / 2)), !sortByX, boundingBox);
            rightBranch = new RTree(branch.subList(branch.size() / 2, branch.size()), !sortByX, boundingBox);
        } else {
            leafElements = mapElements;
        }
    }

    public List<HasBoundingBox> boundsSearch(Bounds rect){
        if(leafElements != null){
            return leafElements;
        } else {
            List<HasBoundingBox> foundElements = new ArrayList<>();
            if(rect.overlaps(leftBranch.getBoundingBox())){
                foundElements.addAll(leftBranch.boundsSearch(rect));
            }
            if(rect.overlaps(rightBranch.getBoundingBox())){
                foundElements.addAll(rightBranch.boundsSearch(rect));
            }
            return foundElements;
        }
    }

    public void validate() {
        if (leafElements != null) {
            resizeBoundingBox(leafElements);
            return;
        }

        if (leftBranch != null) {
            leftBranch.validate();
            resizeBoundingBox(leftBranch);
        }

        if (rightBranch != null) {
            rightBranch.validate();
            resizeBoundingBox(rightBranch);
        }
    }

    public List<HasBoundingBox> sortByX(List<HasBoundingBox> mapElements){
        mapElements.sort(Comparator.comparingDouble(o -> o.getBoundingBox().getMinX()));
        return mapElements;
    }

    public List<HasBoundingBox> sortByY(List<HasBoundingBox> mapElements){
        mapElements.sort(Comparator.comparingDouble(o -> o.getBoundingBox().getMinY()));
        return mapElements;
    }

    private void resizeBoundingBox(List<HasBoundingBox> elements) {
        for (HasBoundingBox element : elements) {
            resizeBoundingBox(element);
        }
    }

    private void resizeBoundingBox(HasBoundingBox element) {
        Bounds bounds = element.getBoundingBox();

        float elementMinX = bounds.getMinX();
        float elementMaxX = bounds.getMaxX();
        float elementMinY = bounds.getMinY();
        float elementMaxY = bounds.getMaxY();

        if (elementMinX < boundingBox.getMinX()) {
            boundingBox.setMinX(elementMinX);
        }

        if (elementMaxX > boundingBox.getMaxX()) {
            boundingBox.setMaxX(elementMaxX);
        }

        if (elementMinY < boundingBox.getMinY()) {
            boundingBox.setMinY(elementMinY);
        }

        if (elementMaxY > boundingBox.getMaxY()) {
            boundingBox.setMaxY(elementMaxY);
        }
    }

    @Override
    public Bounds getBoundingBox() {
        return boundingBox;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double x = boundingBox.getMinX();
        double y = boundingBox.getMinY();
        double w = boundingBox.getMaxX() - boundingBox.getMinX();
        double h = boundingBox.getMaxY() - boundingBox.getMinY();
        gc.strokeRect(x, y, w, h);
    }
}