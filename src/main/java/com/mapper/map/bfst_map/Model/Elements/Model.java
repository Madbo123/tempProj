package com.mapper.map.bfst_map.Model.Elements;

import com.mapper.map.bfst_map.Controller.AddressSearcher.RadixTree;
import com.mapper.map.bfst_map.Controller.Dijkstra.*;
import com.mapper.map.bfst_map.Controller.RTree.RTreeController;
import com.mapper.map.bfst_map.Model.Dijkstra.EarlyRoad;
import com.mapper.map.bfst_map.Model.Dijkstra.Vertex;
import javafx.geometry.Point2D;

import javax.xml.stream.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipInputStream;

public class Model implements Serializable {
    public static List<Line> lines = new ArrayList<>();
    public static List<Way> ways = new ArrayList<>();
    public static List<Relation> relations = new ArrayList<>();
    public static List<Way> taggedWays = new ArrayList<>();
    public static DijkstraController dijkstraController;
    public static RadixTree<Waypoint> addressTST = new RadixTree<>();
    public static RTreeController rTreeController;
    double minLat, maxLat, minLon, maxLon;
    float lat, lon = 0;

    public static Model load(String filename) throws IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".obj")) {
            try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }

    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } else {
            parseTXT(filename);
        }
        save(filename+".obj");
    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        ZipInputStream input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    private void parseOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        parseOSM(new FileInputStream(filename));
    }


    private void prototypeParseCoords(XMLStreamReader input) {
        Set<Node> nodeSet = new HashSet<>();

        //Ny node med id koblet
        //nodeSet.add(node);

        //måske hashtable...

        //eller kan vi slippe afsted med et associativt array?
    }


    private void parseOSM(InputStream inputStream) throws XMLStreamException, FactoryConfigurationError {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HashMap<Long, Node> id2node = new HashMap<>();
        HashMap<Long, Way> id2way = new HashMap<>();
        HashMap<Long, Vertex> id2vertex = new HashMap<>();
        HashMap<String, String> nodeTags = new HashMap<>();

        ArrayList<Node> wayList = new ArrayList<>();
        HashMap<String, String> wayTags = new HashMap<>();
        ArrayList<Long> refList = new ArrayList<>();

        ArrayList<Node> relationNodes = new ArrayList<>();
        ArrayList<Way> relationWays = new ArrayList<>();
        HashMap<String, String> relationTags = new HashMap<>();
        HashMap<Way, String> wayRoles = new HashMap<>();
        ArrayList<EarlyRoad> earlyRoads = new ArrayList<>();


        long way_id = 0;

        while (input.hasNext()) {

            int tagKind = input.next();

            if (tagKind == XMLStreamConstants.START_ELEMENT) {

                switch (input.getLocalName()) {
                    case "bounds" -> parseBound(input);
                    case "node" -> parseNode(input, id2node);
                    case "way" -> {
                        way_id = Long.parseLong(input.getAttributeValue(null, "id"));
                        parseWay(input, wayList, refList, wayTags);
                    }
                    case "relation" -> parseRelation(relationNodes, relationWays, relationTags, wayRoles);
                    case "member" -> parseMember(input, relationNodes, relationWays, wayRoles, id2node, id2way);
                    case "tag" -> parseTags(input, wayTags, relationTags, nodeTags);
                    case "nd" -> {
                        long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                        wayList.add(id2node.get(ref));
                        refList.add(ref);
                    }
                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {

                String name = input.getLocalName();

                if (Objects.equals(name, "way")) {
                    if (way_id != 0) {
                        if (wayTags.containsKey("highway")){
                            EarlyRoad road = new EarlyRoad(wayList, refList, wayTags);
                            earlyRoads.add(road);
                            for (Long cursor : refList) {

                                if (id2vertex.containsKey(cursor)) {
                                    id2vertex.get(cursor).addRoad();
                                } else {
                                    id2vertex.put(cursor, new Vertex());
                                }
                            }
                        } else {
                            Way tempWay = new Way(wayList, wayTags);

                            id2way.put(way_id, tempWay);
                        }
                        way_id = 0;
                    }


                } else if (Objects.equals(name, "relation")) {


                    relations.add(new Relation(relationWays, relationNodes, relationTags, wayRoles));

                    //Vi tegner alle ways indeholdt i relations via relation-drawing. Generiske ways tegnes til sidst og kan derfor fjernes fra way-listen der er generisk.
                    for (Way way : relationWays) {
                        if (wayRoles.get(way).equals("outer") | wayRoles.get(way).equals("inner")) {
                            taggedWays.add(way);
                        } else {
                            taggedWays.add(way);
                        }
                    }
                } else if (Objects.equals(name, "node")) {
                    if (nodeTags.containsKey("addr:street")){
                        nodeTags.put("lat", ""+lat);
                        nodeTags.put("lon", ""+lon);
                        new Waypoint(nodeTags);
                        nodeTags.clear();
                    }
                }
            }
        }

        ways.addAll(id2way.values());
        dijkstraController = new DijkstraController(earlyRoads, id2vertex);
        rTreeController = new RTreeController(ways, relations, dijkstraController.getRoads());

        try {
            dijkstraController.calculateShortestPath("Nordvangen", "Rævehøjen", "car");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for (Way way : ways) {
            way.clearWayNodes();
        }

        ways.removeAll(taggedWays);

        for (Relation relation : relations) {
            relation.clearNodeMembers();
        }
    }

    private void parseBound(XMLStreamReader input) {
        minLat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
        maxLat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
        minLon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
        maxLon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
    }

    private void parseNode(XMLStreamReader input, HashMap<Long, Node> id2node) {
        long node_id = Long.parseLong(input.getAttributeValue(null, "id"));
        lat = Float.parseFloat(input.getAttributeValue(null, "lat"));
        lon = Float.parseFloat(input.getAttributeValue(null, "lon"));
        id2node.put(node_id, new Node(lat, lon));
    }

    private void parseWay(XMLStreamReader input, ArrayList<Node> way, ArrayList<Long> refList, HashMap<String, String> wayTags) {
        way.clear();
        wayTags.clear();
        refList.clear();
    }

    private void parseRelation(ArrayList<Node> relationNodes, ArrayList<Way> relationWays, HashMap<String, String> relationTags, HashMap<Way, String> wayRoles) {
        relationNodes.clear();
        relationWays.clear();
        relationTags.clear();
        wayRoles.clear();
    }
    private void parseMember(XMLStreamReader input, ArrayList<Node> relationNodes, ArrayList<Way> relationWays, HashMap<Way, String> wayRoles, HashMap<Long, Node> id2node, HashMap<Long, Way> id2way) {
        String type = input.getAttributeValue(null, "type");
        if (type.equals("way")) {
            long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
            String role = input.getAttributeValue(null, "role");

            if (!role.equals("") && id2way.get(ref) != null) {
                wayRoles.put(id2way.get(ref), role);
                relationWays.add(id2way.get(ref));
            } else if (id2way.get(ref) != null) {
                wayRoles.put(id2way.get(ref), "none");
                relationWays.add(id2way.get(ref));
            }

        } else if (type.equals("node")) {
            long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
            relationNodes.add(id2node.get(ref));
        }
    }

    private void parseTags(XMLStreamReader input, HashMap<String, String> wayTags, HashMap<String, String> relationTags, HashMap<String, String> nodeTags) {
        String k = input.getAttributeValue(null, "k");
        String v = input.getAttributeValue(null, "v");

        wayTags.put(k, v);
        relationTags.put(k, v);
        nodeTags.put(k, v);
    }

    private void parseTXT(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                lines.add(new Line(s.nextLine()));
            }
        }
    }

    public void add(Point2D p1, Point2D p2) {
        lines.add(new Line(p1, p2));
    }

    public double getMinLat() {
        return minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public static List<Relation> getRelations() {
        return relations;
    }

    public static List<Line> getLines() {
        return lines;
    }

    public static List<Way> getTaggedWays() {
        return taggedWays;
    }

    public static List<Way> getWays() {
        return ways;
    }

    /*private void parseOSM(InputStream inputStream) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        XMLStreamReader input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        HashMap<Long, Node> id2node = new HashMap<>();
        HashMap<Long, Way> id2way = new HashMap<>();

        ArrayList<Node> wayList = new ArrayList<>();
        HashMap<String, String> wayTags = new HashMap<>();

        ArrayList<Node> relationNodes = new ArrayList<>();
        ArrayList<Way> relationWays = new ArrayList<>();
        HashMap<String, String> relationTags = new HashMap<>();
        HashMap<Way, String> wayRoles = new HashMap<>();

        long way_id = 0;


        while (input.hasNext()) {

            int tagKind = input.next();

            if (tagKind == XMLStreamConstants.START_ELEMENT) {


                String name = input.getLocalName();

                if (Objects.equals(name, "bounds")) {

                    parseBound(input);

                } else if (Objects.equals(name, "node")) {

                    parseNode(input, id2node);

                } else if (Objects.equals(name, "way")) {

                    way_id = Long.parseLong(input.getAttributeValue(null, "id"));
                    parseWay(input, wayList, wayTags);


                } else if (Objects.equals(name, "relation")) {

                    parseRelation(relationNodes, relationWays, relationTags, wayRoles);


                } else if (Objects.equals(name, "member")) {

                    parseMember(input, relationNodes, relationWays, wayRoles, id2node, id2way);

                } else if (Objects.equals(name, "tag")) {

                    parseTags(input, wayTags, relationTags);


                } else if (Objects.equals(name, "nd")) {

                    long ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    wayList.add(id2node.get(ref));

                }

            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {

                String name = input.getLocalName();

                if (Objects.equals(name, "way")) {
                    if (way_id != 0) {
                        Way tempWay = new Way(wayList, wayTags);

                        tempWay.assignID(way_id);
                        id2way.put(way_id, tempWay);

                        way_id = 0;
                    }


                } else if (Objects.equals(name, "relation")) {


                    relations.add(new Relation(relationWays, relationNodes, relationTags, wayRoles));

                    //Vi tegner alle ways indeholdt i relations via relation-drawing. Generiske ways tegnes til sidst og kan derfor fjernes fra way-listen der er generisk.
                    for (Way way : relationWays) {
                        if (wayRoles.get(way).equals("outer") | wayRoles.get(way).equals("inner")) {
                            taggedWays.add(way);
                        }
                    }
                }
            }
        }
        ways.addAll(id2way.values());
    } */
}
