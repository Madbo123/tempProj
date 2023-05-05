package com.mapper.map.bfst_map.Model.Dijkstra;

import com.mapper.map.bfst_map.Model.Elements.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EarlyRoad {
    List<Node> nodes;
    List<Long> nodeIDs;
    Map<String, String> tags;

    public EarlyRoad(List<Node> nodes, List<Long> nodeIDs, Map<String, String> tags){
        this.nodes = new ArrayList<>(nodes);
        this.tags = new HashMap<>(tags);
        this.nodeIDs = new ArrayList<>(nodeIDs);
    }

    public List<Long> getPath(){
        return nodeIDs;
    }

    public Map<String, String> getTags(){
        return tags;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}