package com.gmail.tthiagoaze.route.service;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.service.impl.RouteCacheServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteCacheServiceTest {

    @InjectMocks
    private RouteCacheServiceImpl service;

    private final String DEPARTURE = "AAA";
    private final String ARRIVAL = "BBB";
    private final String ROUTE_DESCRIPTION = "AAA - CCC - BBB";
    private final Integer ROUTE_PRICE = 50;
    private final String MAX_ENTRIES_VARIABLE_NAME = "maxEntries";
    private final Integer MAX_ENTRIES_VALUE = 2;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(service, MAX_ENTRIES_VARIABLE_NAME, MAX_ENTRIES_VALUE);
    }

    @Test
    public void shouldNotGetCheaperRouteFromCache() {
        RouteResponseDTO cheaperRoute = service.getCheaperRoute(DEPARTURE, ARRIVAL);
        assertNull(cheaperRoute);
    }

    @Test
    public void shouldGetCheaperRouteFromCacheAfterSave() {
        service.saveCheaperRouteCache(DEPARTURE, ARRIVAL,
                RouteResponseDTO.builder().description(ROUTE_DESCRIPTION).price(ROUTE_PRICE).build());

        RouteResponseDTO cheaperRoute = service.getCheaperRoute(DEPARTURE, ARRIVAL);
        assertNotNull(cheaperRoute);
        assertEquals(ROUTE_DESCRIPTION, cheaperRoute.getDescription());
        assertEquals(ROUTE_PRICE.longValue(), cheaperRoute.getPrice().longValue());
    }

    @Test
    public void shouldCleanCacheWithSuccess() {
        service.saveCheaperRouteCache(DEPARTURE, ARRIVAL, RouteResponseDTO.builder().build());
        service.cleanCache();

        RouteResponseDTO cheaperRoute = service.getCheaperRoute(DEPARTURE, ARRIVAL);
        assertNull(cheaperRoute);
    }

    @Test
    public void shouldKeepInCacheOnlyQuantitySetUp() {
        RouteResponseDTO cheaperRoute = null;
        String sufix = "";
        for (int i = 0; i < 3; i++) {
            sufix = String.valueOf(i);
            service.saveCheaperRouteCache(DEPARTURE.concat(sufix), ARRIVAL.concat(sufix),
                    RouteResponseDTO.builder().build());

            cheaperRoute = service.getCheaperRoute(DEPARTURE.concat(sufix), ARRIVAL.concat(sufix));
            assertNotNull(cheaperRoute);
        }

        sufix = "0";
        cheaperRoute = service.getCheaperRoute(DEPARTURE.concat(sufix), ARRIVAL.concat(sufix));
        assertNull(cheaperRoute);
    }

}
