package com.gmail.tthiagoaze.route.repository.impl;

import com.gmail.tthiagoaze.route.exception.GenericErrorException;
import com.gmail.tthiagoaze.route.exception.NotFoundException;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.repository.RouteRepository;
import com.gmail.tthiagoaze.route.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RouteRepositoryImpl implements RouteRepository {

    @Autowired
    @Qualifier("routeFile")
    private String filePath;

    @Override
    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!StringUtils.isEmpty(line)) {
                    Route route = createRouteEntity(line);
                    routes.add(route);
                }
            }

        } catch (FileNotFoundException e) {
            log.error("File not found: {}. {}", filePath, e);
            throw new NotFoundException(String.format("File not found: %s", filePath));
        } catch (IOException e) {
            log.error("Error reading route file {}. {}", filePath, e);
            throw new GenericErrorException(String.format("Error reading routes. %s", e.getMessage()));
        }

        return routes;
    }

    @Override
    public Optional<Route> findRoute(String departure, String arrival) {
        List<Route> routes = getAllRoutes();
        return routes.stream().filter(r -> r.getArrival().equalsIgnoreCase(arrival) &&
                r.getDeparture().equalsIgnoreCase(departure)).findFirst();
    }

    @Override
    public void saveNewRoute(Route route) {
        try (FileWriter fileWriter = new FileWriter(filePath, Boolean.TRUE);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.newLine();
            writer.write(route.getRouteToFile());
        } catch (FileNotFoundException e) {
            log.error("File not found: {}. {}", filePath, e);
            throw new NotFoundException(String.format("File not found: %s", filePath));
        } catch (IOException e) {
            log.error("Error saving new route to file {}. {}", filePath, e);
            throw new GenericErrorException(String.format("Error saving new route to file. %s", e.getMessage()));
        }
    }

    private Route createRouteEntity(String line) {
        String[] routeData = line.split(Constants.COMMA_DELIMITER);
        return Route.builder()
                .departure(routeData[Constants.DEPARTURE_FILE_POSITION])
                .arrival(routeData[Constants.ARRIVAL_FILE_POSITION])
                .price(Integer.parseInt(routeData[Constants.PRICE_FILE_POSITION]))
                .build();
    }
}
