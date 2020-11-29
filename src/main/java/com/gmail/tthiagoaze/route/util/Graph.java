package com.gmail.tthiagoaze.route.util;

import com.gmail.tthiagoaze.route.model.Node;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;

public class Graph {

    private Map<Node, LinkedHashSet<Node>> nodes = new HashMap();

    public void addEdge(Node nodeFrom, Node nodeTo) {
        LinkedHashSet<Node> adjacent = nodes.get(nodeFrom);
        if (adjacent == null) {
            adjacent = new LinkedHashSet();
            nodes.put(nodeFrom, adjacent);
        }
        adjacent.add(nodeTo);
    }

    public LinkedList<Node> adjacentNodes(Node last) {
        LinkedHashSet<Node> adjacent = nodes.get(last);
        if (adjacent == null) {
            return new LinkedList();
        }
        return new LinkedList<>(adjacent);
    }
}
