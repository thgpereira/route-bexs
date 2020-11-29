package com.gmail.tthiagoaze.route.controller;

import com.gmail.tthiagoaze.route.dto.request.RouteRequestDTO;
import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping(value = "/route")
public class RouteController {

    private RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/{departure}/{arrival}")
    public RouteResponseDTO findCheaperRoute(@PathVariable @NotEmpty String departure,
                                             @PathVariable @NotEmpty String arrival) {
        return routeService.findCheaperRoute(departure, arrival);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createNewRoute(@RequestBody @Valid RouteRequestDTO routeRequestDTO) {
        routeService.saveNewRoute(routeRequestDTO);
    }

}
