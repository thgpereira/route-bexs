package com.gmail.tthiagoaze.route.service.impl;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.model.Node;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.service.RouteProcessService;
import com.gmail.tthiagoaze.route.util.Constants;
import com.gmail.tthiagoaze.route.util.Graph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class RouteProcessServiceImpl implements RouteProcessService {

    @Override
    public List<RouteResponseDTO> generateAllPossibleRoutes(String departure, String arrival, List<Route> routes) {
        log.info("Getting routes. From: {} - To: {}", departure, arrival);
        Graph graph = createGraph(routes);
        List<RouteResponseDTO> routeResponseDTOS = new ArrayList<>();
        LinkedList<Node> visitedRoute = new LinkedList();
        visitedRoute.add(Node.builder().name(departure).value(Constants.ZERO).build());
        createRoutes(arrival, graph, routeResponseDTOS, visitedRoute);
        log.info("{} routes found. From: {} - To: {}", routeResponseDTOS.size(), departure, arrival);
        return routeResponseDTOS;
    }

    private Graph createGraph(List<Route> routes) {
        Graph graph = new Graph();
        routes.forEach(r -> graph.addEdge(r.getNodeFrom(), r.getNoteTo()));
        return graph;
    }

    private void createRoutes(String arrival, Graph graph, List<RouteResponseDTO> routeResponseDTO,
                              LinkedList<Node> visitedRoute) {
        LinkedList<Node> nodes = graph.adjacentNodes(visitedRoute.getLast());
        processArrivalNode(arrival, routeResponseDTO, visitedRoute, nodes);
        processAdjacentNodes(arrival, graph, routeResponseDTO, visitedRoute, nodes);
    }

    private void processArrivalNode(String arrival, List<RouteResponseDTO> routeResponseDTO,
                                    LinkedList<Node> visitedRoute, LinkedList<Node> nodes) {
        Optional<Node> nodeArrival = nodes.stream().filter(node -> node.getName().equals(arrival)).findFirst();
        if (nodeArrival.isPresent()) {
            visitedRoute.add(nodeArrival.get());
            addRouteToList(routeResponseDTO, visitedRoute);
            visitedRoute.removeLast();
        }
    }

    private void processAdjacentNodes(String arrival, Graph graph, List<RouteResponseDTO> routeResponseDTO,
                                      LinkedList<Node> visitedRoute, LinkedList<Node> nodes) {
        for (Node node : nodes) {
            if (visitedRoute.contains(node) || node.getName().equals(arrival)) {
                continue;
            }
            visitedRoute.addLast(node);
            createRoutes(arrival, graph, routeResponseDTO, visitedRoute);
            visitedRoute.removeLast();
        }
    }

    private void addRouteToList(List<RouteResponseDTO> routeResponseDTO, LinkedList<Node> visitedRoute) {
        AtomicReference<Integer> routePrice = new AtomicReference<>(Constants.ZERO);
        StringJoiner routeDescription = new StringJoiner(Constants.ROUTE_DELIMITER_OUTPUT);
        visitedRoute.forEach(vr -> {
            routePrice.updateAndGet(price -> price + vr.getValue());
            routeDescription.add(vr.getName());
        });
        log.info("Route added: {} > {}", routeDescription, routePrice);
        routeResponseDTO.add(RouteResponseDTO.builder().description(routeDescription.toString()).price(routePrice
                .get()).build());
    }

}
