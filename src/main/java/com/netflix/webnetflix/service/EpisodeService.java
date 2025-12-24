package com.netflix.webnetflix.service;


import com.netflix.webnetflix.dal.EpisodeDao;
import com.netflix.webnetflix.entity.Episode;
import com.netflix.webnetflix.service.exception.EpisodeNotFoundException;
import com.netflix.webnetflix.service.exception.SeriesMaxEpisodesException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EpisodeService {

    private final EpisodeDao episodeDao;
    private final int episodeLimit;

    @Autowired
    public EpisodeService(EpisodeDao episodeDao,
                          @Value("${seriesService.episodeLimit}") int episodeLimit) {
        this.episodeDao = episodeDao;
        this.episodeLimit = episodeLimit;
    }

    public List<Episode> getAllEpisode() throws Exception {
        List<Episode> list = this.episodeDao.getAll();
        Collections.sort(list);
        return list;
    }

    public void saveEpisode(@Valid Episode episode) throws Exception{
        List<Episode> all = this.episodeDao.getAll();

        List<Episode> episodesInSeries = all.stream().filter(e->e.getSeriesId() == episode.getSeriesId()).toList();

        if (episodesInSeries.size() > this.episodeLimit) {
            throw new SeriesMaxEpisodesException();
        }
    }

    public void updateEpisode(@Valid Episode episode) throws Exception {
        if(!exists(episode.getId())){
            throw new EpisodeNotFoundException(episode.getId());
        }
        this.episodeDao.update(episode);
    }

    public void deleteEpisode(int id) throws Exception {
         if(!exists(id)){
             throw new EpisodeNotFoundException(id);
         }
        this.episodeDao.delete(id);
    }

    public Episode getEpisode(int id) throws Exception {
        Episode episode = this.episodeDao.get(id);
        if (episode == null) {
            throw new EpisodeNotFoundException(id);
        }
        return episode;
    }

    public boolean exists(int id) throws Exception{
        Episode exiting = this.episodeDao.get(id);

        return exiting != null;
    }
}
