package com.mapper.map.bfst_map.Model.Elements;

import com.mapper.map.bfst_map.Controller.GUI.DrawController;
import com.mapper.map.bfst_map.Model.RTree.Bounds;
import com.mapper.map.bfst_map.Model.RTree.HasBoundingBox;
import com.mapper.map.bfst_map.Utils.Utilities;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.*;


public class Relation extends Waypoint implements Serializable, Comparable<Relation>, HasBoundingBox {
    HashMap<String, String> tags = new HashMap<>();

    HashMap<Way, String> wayRoles = new HashMap<>();

    List<Node> nodeMembers = new ArrayList<>();

    String test_var = "Inderhavnen";

    List<Way> sortedOuters = new ArrayList<>();

    List<Way> sortedInners = new ArrayList<>();

    private final List<Way> sortedWays = new ArrayList<>();

    double minLat;

    double minLon;

    Bounds boundingBox = new Bounds();

    public Relation(List<Way> wayMembers, List<Node> nodeMembers, HashMap<String, String> tags, HashMap<Way, String> wayRoles) {
        super(tags);
        this.nodeMembers.addAll(nodeMembers);


        this.tags.putAll(tags);
        this.wayRoles.putAll(wayRoles);



        //Øerne er dysfunktionelle da osm-filen ikke indeholder alle deres nodes. Der skal laves et fix til sådanne situationer senere - MB


        if (wayMembers.size() >= 1 && !tags.containsValue("Amager") && !tags.containsValue("Christiansholm (Papirøen)") && !tags.containsValue("Zealand")) {
            sortWays(wayMembers);
        }

        temp();
    }



    private void sortWays(List<Way> wayMembers) {
        ArrayList<Way> outer_ways = new ArrayList<>();


        for (Way way : wayMembers) {
            if (wayRoles.get(way).equals("outer")) {
                way.assignTags(tags);
                DrawController.assignColor(way);
                outer_ways.add(way);

            } else if (wayRoles.get(way).equals("inner")) {
                if (!outer_ways.isEmpty()) {
                    if (outer_ways.size() >= 2) {
                        waySorter(outer_ways);
                        outer_ways.clear();
                    } else {
                        sortedOuters.add(outer_ways.get(0));
                        outer_ways.clear();
                    }
                }
                sortedInners.add(way);
            }
        }

        if (!outer_ways.isEmpty()) {
            if (outer_ways.size() >= 2) {
                waySorter(outer_ways);
                outer_ways.clear();
            } else {
                sortedOuters.add(outer_ways.get(0));
                outer_ways.clear();
            }
        }
    }

    //Returns true and allows fusing of points if given points are very close.
    private boolean breakpointFinder(List<Way> ways, Way candidateWay) {
        Node first = candidateWay.getFirstNode();
        Node last = candidateWay.getLastNode();

        for (Way way : ways) {
            if (!way.equals(candidateWay)) {
                Node start = way.getFirstNode();
                Node end = way.getLastNode();

                if (Utilities.nodeDistance(start, first) < 0.005 | Utilities.nodeDistance(start, last) < 0.005 | Utilities.nodeDistance(end, first) < 0.005 | Utilities.nodeDistance(end, last) < 0.005) {
                    return true;
                }
            }
        }

        return false;
    }




    //Afliv mig
    private void waySorter(List<Way> unsortedOuters) {
        ArrayList<Node> sortedNodes = new ArrayList<>();
        ArrayList<ArrayList<Node>> unsortedNodes = new ArrayList<>();


        for (Way way : unsortedOuters) {
            if (!unsortedNodes.isEmpty() && breakpointFinder(unsortedOuters, way)) {
                //Matcher på sig selv
                unsortedNodes.add(way.getWayNodes());
            } else if (unsortedNodes.isEmpty()) {
                unsortedNodes.add(way.getWayNodes());
            } else {
                sortedOuters.add(way);
            }
        }



        while (!unsortedNodes.isEmpty()) {
            nodeSorter(sortedNodes, unsortedNodes);
        }


        sortedOuters.add(new Way(sortedNodes, tags));
    }

    public void nodeSorter(ArrayList<Node> sortedNodes, ArrayList<ArrayList<Node>> unsortedNodes) {
        double minDistance = Double.MAX_VALUE;
        boolean flipNodes = false;
        ArrayList<Node> minList = null;
        Node end = null;


        if (sortedNodes.isEmpty()) {
            ArrayList<Node> ideal = null;
            boolean start_ideal = true;
            boolean end_ideal = true;

            for (ArrayList<Node> nodes1 : unsortedNodes) {
                Node start1 = nodes1.get(0);
                Node end1 = nodes1.get(nodes1.size() - 1);
                start_ideal = true;
                end_ideal = true;
                ideal = nodes1;


                for (ArrayList<Node> nodes2: unsortedNodes) {
                    if (!nodes1.equals(nodes2)) {

                        if (nodes2.contains(start1)) {
                            start_ideal = false;
                        }

                        if (nodes2.contains(end1)) {
                            end_ideal = false;
                        }
                    }
                }

                if (start_ideal | end_ideal) {
                    break;
                }
            }

            if (start_ideal && ideal != null) {
                unsortedNodes.remove(ideal);
                sortedNodes.addAll(ideal);
            } else if (end_ideal && ideal != null) {
                unsortedNodes.remove(ideal);
                Collections.reverse(ideal);
                sortedNodes.addAll(ideal);
            }
        }


        if (sortedNodes.isEmpty()) {
            end = unsortedNodes.get(0).get(unsortedNodes.get(0).size() - 1);
        } else {
            end = sortedNodes.get(sortedNodes.size() - 1);
        }

        if (!unsortedNodes.isEmpty()) {
            for (ArrayList<Node> candidateNodes : unsortedNodes) {
                Node first = candidateNodes.get(0);
                Node second = candidateNodes.get(candidateNodes.size() - 1);


                if (Utilities.nodeDistance(end, first) < minDistance) {
                    minDistance = Utilities.nodeDistance(end, first);
                    minList = candidateNodes;
                    flipNodes = false;
                }

                if (Utilities.nodeDistance(end, second) < minDistance) {
                    minDistance = Utilities.nodeDistance(end, second);
                    minList = candidateNodes;
                    flipNodes = true;
                }
            }


            if (flipNodes) {
                unsortedNodes.remove(minList);
                Collections.reverse(minList);
                sortedNodes.addAll(minList);
            } else {
                unsortedNodes.remove(minList);
                sortedNodes.addAll(minList);
            }


            //unsortedNodes.remove(minList);
        }


        //System.out.println("Found smallest dist: " + minDistance + " - on index: " + minIndex);
    }

    public void temp() {
        sortedWays.addAll(sortedOuters);

        interleave(sortedWays, sortedInners);
    }


    public void interleave(List<Way> a1, List<Way> a2) {
        int i = -1;
        for(Way way: a2) {
            if(i < a1.size()-1) {
                i += 2;
            } else {
                i += 1;
            }
            a1.add(i, way);
        }
    }

    public void draw1(GraphicsContext gc) {
        for (Way way : sortedWays) {
            way.draw(gc);
        }
    }

    public void draw(GraphicsContext gc) {
        for (Way way : sortedOuters) {
            way.draw(gc);
        }

        for (Way way : sortedInners) {
            way.draw(gc);
        }
    }




    public List<Node> getNodeMembers() {
        return nodeMembers;
    }

    public void clearNodeMembers() {
        nodeMembers.clear();
        for (Way way : sortedInners) {
            way.clearWayNodes();
        }

        for (Way way : sortedOuters) {
            way.clearWayNodes();
        }
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public boolean containsTag(String tag) {
        return this.tags.containsKey(tag) | this.tags.containsValue(tag);
    }

    public List<Way> getInners() {
        return sortedInners;
    }

    public List<Way> getOuters() {
        return sortedOuters;
    }

    @Override
    public Bounds getBoundingBox() {
        return boundingBox;
    }

    @Override
    public int compareTo(Relation o) {
        return Double.compare(this.getMinLat(), o.getMinLat());
    }
}