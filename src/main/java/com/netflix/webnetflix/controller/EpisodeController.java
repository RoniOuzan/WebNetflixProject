package com.netflix.webnetflix.controller;

import com.netflix.webnetflix.entity.Episode;
import com.netflix.webnetflix.service.EpisodeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    @Autowired
    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @PostMapping("/action")
    public String handleAction(@RequestParam String action,
                               @RequestParam int seriesId,
                               @RequestParam(required = false) Integer selectedEpId,
                               Model model) throws Exception {

        if (action.equals("Add")) {
            model.addAttribute("episode", new Episode(1, "", "", seriesId));
            model.addAttribute("mode", "add");
            model.addAttribute("seriesId", seriesId);
            return "episode-form";
        }

        if (selectedEpId == null) {
            throw new Exception("You must select an episode first!");
        }

        if (action.equals("Delete")) {
            episodeService.deleteEpisode(selectedEpId);
            return "redirect:/series/view?id=" + seriesId;
        }

        Episode episode = episodeService.getEpisode(selectedEpId);
        model.addAttribute("episode", episode);
        model.addAttribute("seriesId", seriesId);

        if (action.equals("Edit")) {
            model.addAttribute("mode", "edit");
            return "episode-form";
        }

        if (action.equals("Show")) {
            model.addAttribute("mode", "show");
            return "episode-form";
        }

        return "redirect:/series";
    }

    @PostMapping("/save")
    public String saveEpisode(@Valid @ModelAttribute("episode") Episode episode,
                              BindingResult result,
                              @RequestParam String mode,
                              @RequestParam int seriesId,
                              Model model) throws Exception {

        episode.setSeriesId(seriesId);

        if (result.hasErrors()) {
            model.addAttribute("mode", mode);
            model.addAttribute("seriesId", seriesId);
            return "episode-form";
        }

        // Try to save, catch our duplicate error if it fails
        try {
            if (mode.equals("add")) {
                episodeService.saveEpisode(episode);
            } else if (mode.equals("edit")) {
                episodeService.updateEpisode(episode);
            }
        } catch (Exception e) {
            // If duplicate found, send the error message back to the form
            model.addAttribute("mode", mode);
            model.addAttribute("seriesId", seriesId);
            model.addAttribute("errorMessage", e.getMessage());
            return "episode-form";
        }

        return "redirect:/series/view?id=" + seriesId;
    }
}