package com.netflix.webnetflix.cli;

import com.netflix.webnetflix.service.EpisodeService;
import com.netflix.webnetflix.service.SeriesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.netflix.webnetflix")
public class CLI {

    static Scanner scanner;
    static SeriesService seriesService;
    static EpisodeService episodeService;

    public static void main(String[] args) {
        var context = SpringApplication.run(CLI.class, args);
        seriesService = context.getBean("seriesService", SeriesService.class);
        episodeService = context.getBean("episodeService", EpisodeService.class);
        scanner = new Scanner(System.in);
        printMenu();
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Show series list");
        System.out.println("2. Show single series");
        System.out.println("3. Add series");
        System.out.println("4. Update series");
        System.out.println("5. Delete series");
        System.out.println("6. Add episode to series");
        System.out.println("7. Update episode");
        System.out.println("8. show single episode");
        System.out.println("9. Delete episode from series");
        System.out.println("0. Exit");
    }
}
