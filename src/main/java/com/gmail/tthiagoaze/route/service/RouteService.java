package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.request.RouteRequestDTO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;

public interface RouteService {
    RouteResponseDTO findCheaperRoute(String departure, String arrival);

    void saveNewRoute(RouteRequestDTO routeRequestDTO);

}
