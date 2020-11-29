package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.request.RouteRequestDTO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.exception.AlreadyExistsException;
import com.gmail.tthiagoaze.route.exception.NotFoundException;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.repository.RouteRepository;
import com.gmail.tthiagoaze.route.service.impl.RouteServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {

    @InjectMocks
    private RouteServiceImpl service;

    @Mock
    private RouteCacheService routeCacheService;

    @Mock
    private RouteProcessService routeProcessService;

    @Mock
    private RouteRepository routeRepository;

    private final String DEPARTURE = "AAA";
    private final String ARRIVAL = "BBB";
    private final String ROUTE_DESCRIPTION = "AAA - CCC - BBB";
    private final Integer ROUTE_PRICE = 50;
    private final String ROUTE_DESCRIPTION_EXPENSIVE = "AAA - BBB";
    private final Integer ROUTE_PRICE_EXPENSIVE = 60;

    @Test
    public void shouldReturnCheaperRouteFromCache() {
        RouteResponseDTO cacheResponse =
                RouteResponseDTO.builder().description(ROUTE_DESCRIPTION).price(ROUTE_PRICE).build();
        when(routeCacheService.getCheaperRoute(anyString(), anyString())).thenReturn(cacheResponse);

        RouteResponseDTO cheaperRoute = service.findCheaperRoute(DEPARTURE, ARRIVAL);
        assertNotNull(cheaperRoute);
        assertEquals(ROUTE_DESCRIPTION, cheaperRoute.getDescription());
        assertEquals(ROUTE_PRICE.longValue(), cheaperRoute.getPrice().longValue());
        verify(routeCacheService, times(1)).getCheaperRoute(anyString(), anyString());
        verify(routeRepository, times(0)).getAllRoutes();
        verify(routeProcessService, times(0)).generateAllPossibleRoutes(anyString(), anyString(), any());
        verify(routeCacheService, times(0)).saveCheaperRouteCache(anyString(), anyString(), any());
    }

    @Test
    public void shouldReturnCheaperRouteFromRepository() {
        Route route = Route.builder().arrival(ARRIVAL).departure(DEPARTURE).price(ROUTE_PRICE).build();
        when(routeCacheService.getCheaperRoute(anyString(), anyString())).thenReturn(null);
        when(routeRepository.getAllRoutes()).thenReturn(Arrays.asList(route));
        when(routeProcessService.generateAllPossibleRoutes(anyString(), anyString(), any())).thenReturn(
                Arrays.asList(RouteResponseDTO.builder().description(ROUTE_DESCRIPTION).price(ROUTE_PRICE).build(),
                        RouteResponseDTO.builder().description(ROUTE_DESCRIPTION_EXPENSIVE).price(ROUTE_PRICE_EXPENSIVE).build()));

        RouteResponseDTO cheaperRoute = service.findCheaperRoute(DEPARTURE, ARRIVAL);
        assertNotNull(cheaperRoute);
        assertEquals(ROUTE_DESCRIPTION, cheaperRoute.getDescription());
        assertEquals(ROUTE_PRICE.longValue(), cheaperRoute.getPrice().longValue());
        verify(routeCacheService, times(1)).getCheaperRoute(anyString(), anyString());
        verify(routeRepository, times(1)).getAllRoutes();
        verify(routeProcessService, times(1)).generateAllPossibleRoutes(anyString(), anyString(), any());
        verify(routeCacheService, times(1)).saveCheaperRouteCache(anyString(), anyString(), any());
    }

    @Test
    public void shouldReturnCheaperRouteNotFound() {
        when(routeCacheService.getCheaperRoute(anyString(), anyString())).thenReturn(null);
        when(routeRepository.getAllRoutes()).thenReturn(null);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.findCheaperRoute(DEPARTURE,
                ARRIVAL));
        assertEquals(String.format("No routes found: %s - %s", DEPARTURE, ARRIVAL), exception.getMessage());
    }

    @Test
    public void shouldSaveNewRouteWithSuccess() {
        when(routeRepository.findRoute(anyString(), anyString())).thenReturn(Optional.empty());
        doNothing().when(routeCacheService).cleanCache();
        doNothing().when(routeRepository).saveNewRoute(any());

        service.saveNewRoute(RouteRequestDTO.builder().arrival(ARRIVAL).departure(DEPARTURE).price(ROUTE_PRICE).build());

        ArgumentCaptor<Route> routeCaptor = ArgumentCaptor.forClass(Route.class);
        verify(routeRepository, times(1)).findRoute(anyString(), anyString());
        verify(routeCacheService, times(1)).cleanCache();
        verify(routeRepository, times(1)).saveNewRoute(routeCaptor.capture());
        assertEquals(DEPARTURE, routeCaptor.getValue().getDeparture());
        assertEquals(ARRIVAL, routeCaptor.getValue().getArrival());
        assertEquals(ROUTE_PRICE, routeCaptor.getValue().getPrice());
    }

    @Test
    public void shouldReturnRouteAlreadyExistsWhenSaveNewRoute() {
        when(routeRepository.findRoute(anyString(), anyString())).thenReturn(Optional.of(Route.builder().build()));

        RouteRequestDTO routeRequest =
                RouteRequestDTO.builder().arrival(ARRIVAL).departure(DEPARTURE).price(ROUTE_PRICE).build();
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> service.saveNewRoute(routeRequest));
        assertEquals(String.format("Route already exists: %s - %s", DEPARTURE, ARRIVAL), exception.getMessage());

        verify(routeRepository, times(1)).findRoute(anyString(), anyString());
        verify(routeCacheService, times(0)).cleanCache();
        verify(routeRepository, times(0)).saveNewRoute(any());
    }

}
