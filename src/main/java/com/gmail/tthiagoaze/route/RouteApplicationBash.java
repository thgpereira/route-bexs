package com.gmail.tthiagoaze.route;

import com.gmail.tthiagoaze.route.dto.response.RouteResponseDTO;
import com.gmail.tthiagoaze.route.exception.RouteException;
import com.gmail.tthiagoaze.route.service.RouteService;
import com.gmail.tthiagoaze.route.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class RouteApplicationBash implements CommandLineRunner {

    private RouteService routeService;

    private static final String REGEX_INPUT_ROUTE = "\\w{3}-\\w{3}";

    @Autowired
    public RouteApplicationBash(RouteService routeService) {
        this.routeService = routeService;
    }

    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.print("please enter the route: ");
            Scanner scanner = new Scanner(System.in);
            String inputRoute = scanner.nextLine();
            if (isValidInputRoute(inputRoute)) {
                String[] inputRouteSplit = inputRoute.split(Constants.ROUTE_DELIMITER_INPUT);
                String departure = inputRouteSplit[Constants.DEPARTURE_FILE_POSITION];
                String arrival = inputRouteSplit[Constants.ARRIVAL_FILE_POSITION];
                findCheaperRoute(departure, arrival);
            }
        }
    }

    private boolean isValidInputRoute(String inputRoute) {
        boolean matches = inputRoute.matches(REGEX_INPUT_ROUTE);
        if (Boolean.FALSE.equals(matches)) {
            System.out.println(String.format("invalid input: %s. eg valid input: AAA-BBB", inputRoute));
        }
        return matches;
    }

    private void findCheaperRoute(String departure, String arrival) {
        try {
            RouteResponseDTO cheaperRoute = routeService.findCheaperRoute(departure, arrival);
            System.out.println(String.format("best route: %s > $%s", cheaperRoute.getDescription(),
                    cheaperRoute.getPrice()));
        } catch (RouteException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unknown error. Try again.");
        }
    }

}
