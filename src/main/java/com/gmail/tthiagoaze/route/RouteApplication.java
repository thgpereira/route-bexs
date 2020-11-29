package com.gmail.tthiagoaze.route;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class RouteApplication {

    private static String routeFile;

    public static void main(String[] args) {
        if (args.length > 0) {
            if (isValidFile(args[0])) {
                SpringApplication.run(RouteApplication.class, args);
            }
        } else {
            System.out.println("File parameter is required");
        }
    }

    private static boolean isValidFile(String file) {
        File f = new File(file);
        if (!f.exists()) {
            System.out.println("File not exists");
        } else {
            routeFile = file;
        }
        return f.exists();
    }

    @Bean
    public static String routeFile() {
        return routeFile;
    }

}
