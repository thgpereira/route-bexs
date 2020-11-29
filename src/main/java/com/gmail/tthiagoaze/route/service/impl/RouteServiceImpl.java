package com.gmail.tthiagoaze.route.service.impl;

import com.gmail.tthiagoaze.route.dto.request.RouteRequestDTO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.exception.AlreadyExistsException;
import com.gmail.tthiagoaze.route.exception.NotFoundException;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.repository.RouteRepository;
import com.gmail.tthiagoaze.route.service.RouteCacheService;
import com.gmail.tthiagoaze.route.service.RouteProcessService;
import com.gmail.tthiagoaze.route.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RouteServiceImpl implements RouteService {

    private RouteCacheService routeCacheService;
    private RouteProcessService routeProcessService;
    private RouteRepository routeRepository;

    @Autowired
    public RouteServiceImpl(RouteCacheService routeCacheService, RouteProcessService routeProcessService,
                            RouteRepository routeRepository) {
        this.routeCacheService = routeCacheService;
        this.routeProcessService = routeProcessService;
        this.routeRepository = routeRepository;
    }

    @Override
    public RouteResponseDTO findCheaperRoute(String departure, String arrival) {
        log.info("Finding cheaper route. {} - {}.", departure, arrival);
        RouteResponseDTO cheaperRouteResponseDTOCache = routeCacheService.getCheaperRoute(departure, arrival);
        if (cheaperRouteResponseDTOCache != null) {
            log.info("Returning cheaper route from cache. {} - {}.", departure, arrival);
            return cheaperRouteResponseDTOCache;
        }
        return loadCheaperRoute(departure, arrival);
    }

    @Override
    public void saveNewRoute(RouteRequestDTO routeRequestDTO) {
        checkRouteExists(routeRequestDTO);
        log.info("Saving new route: {}.", routeRequestDTO);
        Route route = new Route();
        BeanUtils.copyProperties(routeRequestDTO, route);
        routeCacheService.cleanCache();
        routeRepository.saveNewRoute(route);
        log.info("New route saved: {}.", routeRequestDTO);
    }

    private RouteResponseDTO loadCheaperRoute(String departure, String arrival) {
        List<Route> routes = routeRepository.getAllRoutes();
        RouteResponseDTO cheaperRouteResponseDTOCache = loadCheaperRoute(departure, arrival, routes);
        routeCacheService.saveCheaperRouteCache(departure, arrival, cheaperRouteResponseDTOCache);
        log.info("Returning cheaper route. {} - {}.", departure, arrival);
        return cheaperRouteResponseDTOCache;
    }

    private RouteResponseDTO loadCheaperRoute(String departure, String arrival, List<Route> routes) {
        List<RouteResponseDTO> allRoutes = routeProcessService.generateAllPossibleRoutes(departure, arrival, routes);
        if (CollectionUtils.isEmpty(allRoutes)) {
            throw new NotFoundException(String.format("No routes found: %s - %s", departure, arrival));
        }
        return allRoutes.stream().min(Comparator.comparing(RouteResponseDTO::getPrice)).get();
    }

    private void checkRouteExists(RouteRequestDTO routeRequestDTO) {
        String departure = routeRequestDTO.getDeparture();
        String arrival = routeRequestDTO.getArrival();
        log.info("Checking route exists: {} - {}.", departure, arrival);
        Optional<Route> route = routeRepository.findRoute(departure, arrival);
        if (route.isPresent()) {
            log.info("Route already exists: {} - {}.", departure, arrival);
            throw new AlreadyExistsException(String.format("Route already exists: %s - %s", departure, arrival));
        }
    }

}
