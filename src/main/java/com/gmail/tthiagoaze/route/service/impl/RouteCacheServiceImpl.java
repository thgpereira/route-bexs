package com.gmail.tthiagoaze.route.service.impl;

import com.gmail.tthiagoaze.route.bo.RouteCacheBO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.service.RouteCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
public class RouteCacheServiceImpl implements RouteCacheService {

    @Value("${cache.route.max.entries:50}")
    private Integer maxEntries;
    private LinkedHashMap<RouteCacheBO, RouteResponseDTO> cacheMap;

    public RouteCacheServiceImpl() {
        cacheMap = new LinkedHashMap<RouteCacheBO, RouteResponseDTO>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<RouteCacheBO, RouteResponseDTO> eldest) {
                return size() > maxEntries;
            }
        };
    }

    @Override
    public RouteResponseDTO getCheaperRoute(String departure, String arrival) {
        log.info("Loading route cache. From: {} - To: {}", departure, arrival);
        RouteCacheBO routeCacheBO = createRouteCacheBO(departure, arrival);
        RouteResponseDTO bestRouteResponseDTO = cacheMap.get(routeCacheBO);
        log.info("Route cache results. From: {} - To: {} - Result: {}", departure, arrival, bestRouteResponseDTO);
        return bestRouteResponseDTO;
    }

    @Override
    public void saveCheaperRouteCache(String departure, String arrival, RouteResponseDTO bestRouteResponseDTO) {
        log.info("Saving route cache. From: {} - To: {} - Route: {}.", departure, arrival,
                bestRouteResponseDTO.getDescription());
        RouteCacheBO routeCacheBO = createRouteCacheBO(departure, arrival);
        cacheMap.put(routeCacheBO, bestRouteResponseDTO);
        log.info("Route cache saved. From: {} - To: {} - Route: {}.", departure, arrival,
                bestRouteResponseDTO.getDescription());
    }

    @Override
    public void cleanCache() {
        cacheMap.clear();
        log.info("Routes cache cleaned");
    }

    private RouteCacheBO createRouteCacheBO(String departure, String arrival) {
        return RouteCacheBO.builder()
                .departure(departure)
                .arrival(arrival)
                .build();
    }

}
