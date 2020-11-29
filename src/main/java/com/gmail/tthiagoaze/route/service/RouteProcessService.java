package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.model.Route;

import java.util.List;

public interface RouteProcessService {

    List<RouteResponseDTO> generateAllPossibleRoutes(String departure, String arrival, List<Route> routes);

}
