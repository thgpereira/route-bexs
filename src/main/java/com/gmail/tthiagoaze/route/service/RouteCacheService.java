package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;

public interface RouteCacheService {

	RouteResponseDTO getCheaperRoute(String departure, String arrival);
	
	void saveCheaperRouteCache(String departure, String arrival, RouteResponseDTO bestRouteResponseDTO);

	void cleanCache();
	
}
