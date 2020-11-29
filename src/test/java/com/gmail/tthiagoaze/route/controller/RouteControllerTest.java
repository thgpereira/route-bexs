package com.gmail.tthiagoaze.route.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.tthiagoaze.route.dto.request.RouteRequestDTO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.exception.NotFoundException;
import com.gmail.tthiagoaze.route.service.RouteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RouteController.class)
public class RouteControllerTest {

    @MockBean
    private RouteService service;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    private final String ROUTE_ENDPOINT = "/route";

    @Test
    public void shouldFindCheaperRouteWithSuccess() throws Exception {
        when(service.findCheaperRoute(any(), any())).thenReturn(RouteResponseDTO.builder().build());
        mockMvc.perform(get(ROUTE_ENDPOINT.concat("/AAA/BBB")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCustomExceptionWhenFindCheaperRoute() throws Exception {
        when(service.findCheaperRoute(any(), any())).thenThrow(new NotFoundException("Route not found"));
        mockMvc.perform(get(ROUTE_ENDPOINT.concat("/AAA/BBB")).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("Route not found")));
    }

    @Test
    public void shouldCreateNewRouteWithSuccess() throws Exception {
        RouteRequestDTO routeRequest = RouteRequestDTO.builder().arrival("AAA").departure("BBB").price(10).build();
        doNothing().when(service).saveNewRoute(any(RouteRequestDTO.class));
        mockMvc.perform(post(ROUTE_ENDPOINT).content(mapper.writeValueAsString(routeRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnValidationErrorWhenCreateNewRoute() throws Exception {
        RouteRequestDTO routeRequest = RouteRequestDTO.builder().arrival("AA").departure("BBBB").build();
        doNothing().when(service).saveNewRoute(any(RouteRequestDTO.class));
        mockMvc.perform(post(ROUTE_ENDPOINT).content(mapper.writeValueAsString(routeRequest))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItem("departure size must be 3")))
                .andExpect(jsonPath("$.errors", hasItem("arrival size must be 3")))
                .andExpect(jsonPath("$.errors", hasItem("price is required")));
    }

}
