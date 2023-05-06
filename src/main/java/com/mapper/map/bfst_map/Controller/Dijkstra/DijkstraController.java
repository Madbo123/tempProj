package com.mapper.map.bfst_map.Controller.Dijkstra;

import com.mapper.map.bfst_map.Model.Dijkstra.*;
import com.mapper.map.bfst_map.Model.Elements.Model;
import com.mapper.map.bfst_map.Model.Elements.Node;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class DijkstraController {
    private final List<Road> edges;
    private Dijkstra dijkstra;

    public DijkstraController(List<EarlyRoad> earlyRoads, Map<Long, Vertex> vertexMap) {
        edges = new ArrayList<>();

        constructDijkstra(earlyRoads, vertexMap);
    }

    private void constructDijkstra(List<EarlyRoad> earlyRoads, Map<Long, Vertex> id2vertex){
        HashMap<Long, Integer> idMap = new HashMap<>();

        for (EarlyRoad road: earlyRoads) {
            List<Long> path = road.getPath();
            List<Node> nodes = road.getNodes();

            int from = 0;
            List<Node> newNodes = new ArrayList<>();
            newNodes.add(nodes.get(from));
            for (int i = 1; i < path.size(); i++) {
                newNodes.add(nodes.get(i));

                if (!id2vertex.containsKey(path.get(i))) {
                    continue;
                }

                if (id2vertex.get(path.get(i)).getRoadCount() > 1) {
                    int v = getID(path.get(from), idMap);
                    int w = getID(path.get(i), idMap);

                    edges.add(new Road(v, w, newNodes, road.getTags()));

                    if (!isOneway(road)) {
                        Collections.reverse(newNodes);
                        edges.add(new Road(w, v, newNodes, road.getTags()));
                    }

                    from = i;
                    newNodes = new ArrayList<>();
                    newNodes.add(nodes.get(i));
                }
            }

            int v = getID(path.get(from), idMap);
            int w = getID(path.get(path.size() - 1), idMap);

            edges.add(new Road(v, w, newNodes, road.getTags()));

            if (!isOneway(road)) {
                Collections.reverse(newNodes);
                edges.add(new Road(w, v, newNodes, road.getTags()));
            }
        }

        Graph graph = new Graph(idMap.size());

        for (Road road : edges) {
            graph.addEdge(road);
        }

        dijkstra = new Dijkstra(graph);
    }

    private boolean isOneway(EarlyRoad road) {
        Map<String, String> tags = road.getTags();

        if (tags.containsKey("oneway")) {
            if (tags.get("oneway").equals("yes")) {
                return true;
            }
        }

        if (tags.containsKey("junction")) {
            if (tags.get("junction").equals("roundabout")) {
                return true;
            }
        }

        return false;
    }

    private int getID(long id, HashMap<Long, Integer> idMap) {
        if (idMap.containsKey(id)) {
            return idMap.get(id);
        }

        idMap.put(id, idMap.size());
        return idMap.get(id);
    }

    public void calculateShortestPath(String source, String target, String type) {
        int sourceID = getRoad(source).getFrom();
        int targetID = getRoad(target).getTo();

        dijkstra.shortestPath(sourceID, targetID, type);
    }

    private Road getRoad(String name) {
        if (Model.addressTST.contains(name)) {
            return (Road) Model.addressTST.get(name);
        }

        throw new IllegalArgumentException("Calling getRoad(String name) with invalid string.");
    }

    public List<Road> getRoads() {
        return edges;
    }

    public void drawPath(GraphicsContext graphicsContext) {
        if (dijkstra.hasPath()) {
            for (IEdge edge : dijkstra.getPath()) {
                edge.draw(graphicsContext);
            }
        }
    }

    public double getDistance() {
        return dijkstra.getDistance();
    }

    public List<String> getDirections() {
        List<String> directions = new ArrayList<>();

        if (dijkstra.hasPath()) {
            String name = ((Road) dijkstra.getPath().iterator().next()).getAddress();
            IEdge from = dijkstra.getPath().iterator().next();
            double distance = 0.0;
            for (IEdge edge : dijkstra.getPath()) {
                String newName = ((Road) edge).getAddress();

                if (name.equals(newName)) {
                    distance += edge.getDistance();
                } else {
                    if (distance >= 1) {
                        directions.add(" - Follow " + name + " for " + (Math.round(distance * 1000.0) / 1000.0) + " kilometer.");
                    } else {
                        directions.add(" - Follow " + name + " for " + (Math.round(distance * 1000.0)) + " meter.");
                    }
                    directions.add(" - " + calculateDirection(from, edge) + " onto " + newName + ".");

                    name = newName;
                    distance = 0.0;
                }

                from = edge;
            }

            if (distance >= 1) {
                directions.add(" - Follow " + name + " for " + (Math.round(distance * 1000.0) / 1000.0) + " kilometer.");
            } else {
                directions.add(" - Follow " + name + " for " + (Math.round(distance * 1000.0)) + " meter.");
            }
        }

        return directions;
    }

    private String calculateDirection(IEdge from, IEdge to) {
        float[] fromCoordinates = from.getCoordinates();
        float[] toCoordinates = to.getCoordinates();

        float x1 = fromCoordinates[fromCoordinates.length - 4];
        float y1 = fromCoordinates[fromCoordinates.length - 3];
        float x2 = toCoordinates[0];
        float y2 = toCoordinates[1];
        float x3 = toCoordinates[2];
        float y3 = toCoordinates[3];

        double result =  Math.atan2(y1 - y2, x1 - x2) - Math.atan2(y3 - y2, x3 - x2);

        if (result < -Math.PI) {
            result = (-result) % Math.PI;
        }

        if (result > Math.PI) {
            result  = -(result % Math.PI);
        }

        if (result > Math.PI / 8 && result < Math.PI * 7 / 8) {
            return "Turn right";
        }

        if (result < -Math.PI / 8 && result > -Math.PI * 7 / 8) {
            return "Turn left";
        }

        return "Go straight";
    }
}