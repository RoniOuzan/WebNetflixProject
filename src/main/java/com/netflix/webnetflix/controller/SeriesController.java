package com.netflix.webnetflix.controller;

import com.netflix.webnetflix.entity.Series;
import com.netflix.webnetflix.service.EpisodeService;
import com.netflix.webnetflix.service.SeriesService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/series")
public class SeriesController {

    private final SeriesService seriesService;
    private final EpisodeService episodeService;

    @Autowired
    public SeriesController(SeriesService seriesService, EpisodeService episodeService) {
        this.seriesService = seriesService;
        this.episodeService = episodeService;
    }

    // Show Main Page Table
    @GetMapping
    public String mainPage(Model model, HttpSession session) throws Exception {
        if (session.getAttribute("username") == null) return "redirect:/login";

        model.addAttribute("seriesList", seriesService.getAllSeries());
        model.addAttribute("username", session.getAttribute("username"));
        return "main";
    }

    // Handle Toolbar Actions (Add, Show, Edit, Delete)
    @PostMapping("/action")
    public String handleAction(@RequestParam String action,
                               @RequestParam(required = false) Integer selectedId,
                               Model model) throws Exception {

        if (action.equals("Add")) {
            model.addAttribute("series", new Series("", ""));
            model.addAttribute("mode", "add");
            return "series-form";
        }

        if (selectedId == null) {
            throw new Exception("You must select a series first!");
        }

        if (action.equals("Delete")) {
            seriesService.deleteSeries(selectedId);
            return "redirect:/series";
        }

        Series series = seriesService.getSeries(selectedId);
        model.addAttribute("series", series);

        if (action.equals("Edit")) {
            model.addAttribute("mode", "edit");
            return "series-form";
        }

        if (action.equals("Show")) {
            model.addAttribute("mode", "show");
            model.addAttribute("episodes", episodeService.getEpisodesOfList(selectedId));
            return "series-form";
        }

        return "redirect:/series";
    }

    // Save/Update Form Submission
    @PostMapping("/save")
    public String saveSeries(@Valid @ModelAttribute("series") Series series,
                             BindingResult result,
                             @RequestParam String mode,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("mode", mode);
            return "series-form";
        }

        // Try to save, catch our duplicate error if it fails
        try {
            if (mode.equals("add")) {
                seriesService.saveSeries(series);
            } else if (mode.equals("edit")) {
                seriesService.updateSeries(series);
            }
        } catch (Exception e) {
            // If duplicate found, send the error message back to the form
            model.addAttribute("mode", mode);
            model.addAttribute("errorMessage", e.getMessage());
            return "series-form";
        }

        return "redirect:/series";
    }

    @GetMapping("/view")
    public String viewSeries(@RequestParam int id, Model model) throws Exception {
        model.addAttribute("series", seriesService.getSeries(id));
        model.addAttribute("mode", "show");
        model.addAttribute("episodes", episodeService.getEpisodesOfList(id));
        return "series-form";
    }
}