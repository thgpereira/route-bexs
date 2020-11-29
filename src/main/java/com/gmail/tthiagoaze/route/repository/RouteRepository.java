package com.gmail.tthiagoaze.route.repository;

import com.gmail.tthiagoaze.route.model.Route;

import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    List<Route> getAllRoutes();

    Optional<Route> findRoute(String departure, String arrival);

    void saveNewRoute(Route route);

}
