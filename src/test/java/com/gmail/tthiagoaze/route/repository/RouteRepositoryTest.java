package com.gmail.tthiagoaze.route.repository;

import com.gmail.tthiagoaze.route.exception.NotFoundException;
import com.gmail.tthiagoaze.route.model.Route;
import com.gmail.tthiagoaze.route.repository.impl.RouteRepositoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RouteRepositoryTest {

    @InjectMocks
    private RouteRepositoryImpl repository;

    private final String FILE_VARIABLE_NAME = "filePath";

    @Test
    public void shouldGetAllRoutesFromFileWithSuccess() throws IOException {
        createValidFile();
        List<Route> allRoutes = repository.getAllRoutes();
        assertNotNull(allRoutes);
        assertEquals(7, allRoutes.size());
    }

    @Test
    public void shouldReturnFileNotFoundWhenGetAllRoutes() {
        String filePath = "/file-not-found.csv";
        ReflectionTestUtils.setField(repository, FILE_VARIABLE_NAME, filePath);

        NotFoundException exception = assertThrows(NotFoundException.class, repository::getAllRoutes);
        assertEquals(String.format("File not found: %s", filePath), exception.getMessage());
    }

    @Test
    public void shouldFindExistsRouteWithSuccess() throws IOException {
        createValidFile();
        Optional<Route> route = repository.findRoute("BRC", "SCL");
        assertTrue(route.isPresent());
        assertEquals("BRC", route.get().getDeparture());
        assertEquals("SCL", route.get().getArrival());
        assertEquals(5l, route.get().getPrice().longValue());
    }

    @Test
    public void shouldNotFindRouteWithSuccess() throws IOException {
        createValidFile();
        Optional<Route> route = repository.findRoute("AAA", "BBB");
        assertFalse(route.isPresent());
    }

    @Test
    public void shouldSaveNewRouteWithSuccess() throws IOException {
        createValidFile();
        repository.saveNewRoute(Route.builder().departure("XXX").arrival("ZZZ").price(50).build());

        Optional<Route> route = repository.findRoute("XXX", "ZZZ");
        assertTrue(route.isPresent());
    }

    private void createValidFile() throws IOException {
        List<String> routes = Arrays.asList("GRU,BRC,10", "BRC,SCL,5", "GRU,CDG,75", "GRU,SCL,20", "GRU,ORL,56",
                "ORL,CDG,5", "SCL,ORL,20");
        File tempFile = File.createTempFile("input-routes", ".csv");
        tempFile.deleteOnExit();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(tempFile));
        for (String route : routes) {
            bufferedWriter.write(route);
            bufferedWriter.newLine();
        }
        bufferedWriter.close();

        ReflectionTestUtils.setField(repository, FILE_VARIABLE_NAME, tempFile.getAbsolutePath());
    }

}
