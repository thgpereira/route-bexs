package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.service.impl.RouteProcessServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteProcessServiceTest {

    @InjectMocks
    private RouteProcessServiceImpl service;

    private List<Route> routes;

    private final String DEPARTURE = "AAA";
    private final String ARRIVAL = "BBB";
    private final String ARRIVAL_NOT_FOUND = "EEE";
    private final Integer PRICE = 50;
    private final String ROUTE_DESCRIPTION = "AAA - CCC - BBB";

    @Before
    public void setUp() {
        routes = new ArrayList<>();
        routes.add(Route.builder().departure("AAA").arrival("CCC").price(5).build());
        routes.add(Route.builder().departure("AAA").arrival("BBB").price(25).build());
        routes.add(Route.builder().departure("DDD").arrival("BBB").price(18).build());
        routes.add(Route.builder().departure("CCC").arrival("BBB").price(10).build());
        routes.add(Route.builder().departure("CCC").arrival("DDD").price(8).build());
        routes.add(Route.builder().departure("EEE").arrival("DDD").price(8).build());
    }

    @Test
    public void shouldGenerateAllRoutesWithValuesWithSuccess() {
        List<RouteResponseDTO> routesResponse = service.generateAllPossibleRoutes(DEPARTURE, ARRIVAL, routes);
        assertFalse(routesResponse.isEmpty());
        assertEquals(3, routesResponse.size());
    }

    @Test
    public void shouldGenerateAllRoutesWithNoValuesWithSuccess() {
        List<RouteResponseDTO> routesResponse = service.generateAllPossibleRoutes(DEPARTURE, ARRIVAL_NOT_FOUND, routes);
        assertTrue(routesResponse.isEmpty());
    }

}
