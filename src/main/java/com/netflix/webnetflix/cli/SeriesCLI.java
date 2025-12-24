package com.netflix.webnetflix.cli;

import com.netflix.webnetflix.entity.Episode;
import com.netflix.webnetflix.entity.Series;
import com.netflix.webnetflix.service.SeriesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.netflix.webnetflix")
public class SeriesCLI {

    static Scanner scanner;
    static SeriesService seriesService;

    public static void main(String[] args) {
        var context = SpringApplication.run(SeriesCLI.class, args);
        seriesService = context.getBean("seriesService", SeriesService.class);
        scanner = new Scanner(System.in);
        run();
    }

    private static void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Choose option: ");

            try {
                switch (choice) {
                    case 1 -> showSeriesList();
                    case 2 -> showSingleSeries();
                    case 3 -> addSeries();
                    case 4 -> updateSeries();
                    case 5 -> deleteSeries();
                    case 6 -> addEpisode();
                    case 7 -> deleteEpisode();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Bye");
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Show series list");
        System.out.println("2. Show single series");
        System.out.println("3. Add series");
        System.out.println("4. Update series");
        System.out.println("5. Delete series");
        System.out.println("6. Add episode to series");
        System.out.println("7. Delete episode from series");
        System.out.println("0. Exit");
    }

    /* -------- Series -------- */

    private static void showSeriesList() throws Exception {
        List<Series> list = seriesService.getAllSeries();
        if (list.isEmpty()) {
            System.out.println("No series found");
            return;
        }
        list.forEach(s ->
                System.out.println(s.getId() + " | " + s.getName())
        );
    }

    private static void showSingleSeries() throws Exception {
        int id = readInt("Series id: ");
        Series s = seriesService.getSeries(id);

        System.out.println("ID: " + s.getId());
        System.out.println("Name: " + s.getName());
        System.out.println("Description: " + s.getDescription());

        if (s.getEpisodes().isEmpty()) {
            System.out.println("No episodes");
        } else {
            s.getEpisodes().forEach(e ->
                    System.out.println("Episode " + e.getEpisodeNumber() +
                            ": " + e.getName())
            );
        }
    }

    private static void addSeries() throws Exception {
        String name = readString("Name: ");
        String desc = readString("Description: ");

        Series series = new Series(name, desc);
        seriesService.saveSeries(series);
        System.out.println("Series added");
    }

    private static void updateSeries() throws Exception {
        int id = readInt("Series id: ");
        Series existing = seriesService.getSeries(id);

        String name = readString("New name: ");
        String desc = readString("New description: ");

        existing.setName(name);
        existing.setDescription(desc);

        seriesService.updateSeries(existing);
        System.out.println("Series updated");
    }

    private static void deleteSeries() throws Exception {
        int id = readInt("Series id: ");
        seriesService.deleteSeries(id);
        System.out.println("Series deleted");
    }

    /* -------- Episodes -------- */

    private static void addEpisode() throws Exception {
        int seriesId = readInt("Series id: ");
        Series series = seriesService.getSeries(seriesId);

        int epNum = readInt("Episode number: ");
        String name = readString("Episode name: ");
        String desc = readString("Episode description: ");

        seriesService.addEpisode(series, new Episode(epNum, name, desc));

        System.out.println("Episode added");
    }

    private static void deleteEpisode() throws Exception {
        int seriesId = readInt("Series id: ");
        Series series = seriesService.getSeries(seriesId);

        int epId = readInt("Episode id: ");
        series.getEpisodes().removeIf(e -> e.getId() == epId);

        seriesService.updateSeries(series);
        System.out.println("Episode deleted");
    }

    /* -------- Helpers -------- */

    private static int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private static String readString(String msg) {
        System.out.print(msg);
        return scanner.nextLine();
    }
}


