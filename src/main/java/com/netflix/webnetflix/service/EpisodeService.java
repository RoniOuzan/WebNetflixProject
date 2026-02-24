package com.netflix.webnetflix.service;


import com.netflix.webnetflix.dal.EpisodeDao;
import com.netflix.webnetflix.entity.Episode;
import com.netflix.webnetflix.service.exception.EpisodeNotFoundException;
import com.netflix.webnetflix.service.exception.SeriesMaxEpisodesException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EpisodeService {

    private final EpisodeDao episodeDao;
    private final int episodeLimit;
    private final ValidationService validationService;

    @Autowired
    public EpisodeService(EpisodeDao episodeDao,
                          @Value("${seriesService.episodeLimit}") int episodeLimit,
                          ValidationService validationService) {
        this.episodeDao = episodeDao;
        this.episodeLimit = episodeLimit;
        this.validationService = validationService;
    }

    public List<Episode> getAllEpisode() throws Exception {
        List<Episode> list = this.episodeDao.getAll();
        Collections.sort(list);
        return list;
    }

    public void saveEpisode(@Valid Episode episode) throws Exception {
        List<Episode> all = this.episodeDao.getAll();
        List<Episode> episodesInSeries = all.stream().filter(e -> e.getSeriesId() == episode.getSeriesId()).toList();

        // Check for duplicates
        for (Episode existing : episodesInSeries) {
            if (existing.getEpisodeNumber() == episode.getEpisodeNumber()) {
                throw new Exception("An episode with number " + episode.getEpisodeNumber() + " already exists in this series.");
            }
            if (existing.getName().equalsIgnoreCase(episode.getName())) {
                throw new Exception("An episode with the name '" + episode.getName() + "' already exists.");
            }
        }

        if (episodesInSeries.size() >= this.episodeLimit) {
            throw new SeriesMaxEpisodesException();
        }

        validationService.validate(episode);
        this.episodeDao.save(episode);
    }

    public void updateEpisode(@Valid Episode episode) throws Exception {
        if(!exists(episode.getId())){
            throw new EpisodeNotFoundException(episode.getId());
        }

        List<Episode> all = this.episodeDao.getAll();
        // Get all episodes in this series EXCEPT the one we are currently editing
        List<Episode> otherEpisodesInSeries = all.stream()
                .filter(e -> e.getSeriesId() == episode.getSeriesId() && e.getId() != episode.getId())
                .toList();

        // Check for duplicates against other episodes
        for (Episode existing : otherEpisodesInSeries) {
            if (existing.getEpisodeNumber() == episode.getEpisodeNumber()) {
                throw new Exception("An episode with number " + episode.getEpisodeNumber() + " already exists in this series.");
            }
            if (existing.getName().equalsIgnoreCase(episode.getName())) {
                throw new Exception("An episode with the name '" + episode.getName() + "' already exists.");
            }
        }

        validationService.validate(episode);
        this.episodeDao.update(episode);
    }

    public void deleteEpisode(int id) throws Exception {
         if(!exists(id)){
             throw new EpisodeNotFoundException(id);
         }
        this.episodeDao.delete(id);
    }

    public List<Episode> getEpisodesOfList(int id) throws Exception{
        List<Episode> all = this.episodeDao.getAll();

        List<Episode> episodesInSeries = new ArrayList<>(all.stream().filter(e->e.getSeriesId() == id).toList());

        Collections.sort(episodesInSeries);
        return episodesInSeries;
    }

    public Episode getEpisode(int id) throws Exception {
        Episode episode = this.episodeDao.get(id);
        if (episode == null) {
            throw new EpisodeNotFoundException(id);
        }
        return episode;
    }

    private boolean exists(int id) throws Exception{
        Episode exiting = this.episodeDao.get(id);

        return exiting != null;
    }
}
