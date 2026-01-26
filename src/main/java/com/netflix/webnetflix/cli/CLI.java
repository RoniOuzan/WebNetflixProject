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
public class CLI {

    static Scanner scanner;
    static SeriesService seriesService;
//    static EpisodeService episodeService;

    public static void main(String[] args) {
        var context = SpringApplication.run(CLI.class, args);
        seriesService = context.getBean("seriesService", SeriesService.class);
//        episodeService = context.getBean("episodeService", EpisodeService.class);
        scanner = new Scanner(System.in);
        run();
    }
    private static void run() {
        boolean running = true;

        while (running) {
            printMenu();


            try {
                int choice = readInt("Choose option: ");
                switch (choice) {
                    case 1 -> showSeriesList();
                    case 2 -> showSingleSeries();
                    case 3 -> addSeries();
                    case 4 -> updateSeries();
                    case 5 -> deleteSeries();
                    case 6 -> addEpisode();
                    case 7 -> updateEpisode();
                    case 8 -> showSingleEpisode();
                    case 9 -> deleteEpisode();
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
        System.out.println("7. Update episode");
        System.out.println("8. show single episode");
        System.out.println("9. Delete episode from series");
        System.out.println("0. Exit");
    }


    private static void showSeriesList() throws Exception {
        List<Series> list = seriesService.getAllSeries();
        if (list.isEmpty()) {
            System.out.println("No series found");
            return;
        }
        list.forEach(System.out::println);
    }

    private static void showSingleSeries() throws Exception {
        int id = readInt("Series id: ");
        Series s = seriesService.getSeries(id);

        System.out.println("ID: " + s.getId());
        System.out.println("Name: " + s.getName());
        System.out.println("Description: " + s.getDescription());

        List<Episode> episodes = s.getEpisodes();
        if (episodes.isEmpty()) {
            System.out.println("No episodes");
        } else {
            episodes.forEach(System.out::println);
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

    /* -------- Episode -------- */

    private static void addEpisode() throws Exception {
        int seriesId = readInt("Series id: ");
        Series series = seriesService.getSeries(seriesId);

        int epNum = readInt("Episode number: ");
        String name = readString("Episode name: ");
        String desc = readString("Episode description: ");

        Episode episode = new Episode(epNum, name, desc);
//        episodeService.saveEpisode(episode);

        // TOOD: check this
        series.addEpisode(episode);
        seriesService.updateSeries(series);

        System.out.println("Episode added");
    }

    private static void deleteEpisode() throws Exception {
        int epId = readInt("Episode id: ");
        // TODO
//        Episode episode = episodeService.getEpisode(epId);

//        episodeService.deleteEpisode(episode.getId());
        System.out.println("Episode deleted");
    }

    private static void updateEpisode() throws Exception{
        int id = readInt("episode id: ");
        // TODO
//        Episode existing = episodeService.getEpisode(id);

//        int epNum = readInt("New episode number: ");
//        String name = readString("New name: ");
//        String desc = readString("New description: ");
//
//        existing.setEpisodeNumber(epNum);
//        existing.setName(name);
//        existing.setDescription(desc);
//
//        episodeService.updateEpisode(existing);
        System.out.println("Series updated");
    }

    private static void showSingleEpisode() throws Exception{
        int id = readInt("Episode id: ");
        // TODO
//        Episode e = episodeService.getEpisode(id);

//        System.out.println(e);
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
