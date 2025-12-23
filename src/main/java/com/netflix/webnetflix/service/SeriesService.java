package com.netflix.webnetflix.service;

import com.netflix.webnetflix.dal.SeriesDao;
import com.netflix.webnetflix.entity.Series;
import com.netflix.webnetflix.service.exception.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SeriesService {

    private final SeriesDao seriesDao;

    @Autowired
    public SeriesService(SeriesDao seriesDao) {
        this.seriesDao = seriesDao;
    }

    public List<Series> getAllSeries() throws Exception {
        List<Series> list = this.seriesDao.getAll();
        Collections.sort(list);
        return list;
    }

    public void saveSeries(@Valid Series series) throws Exception {
        List<Series> all = this.seriesDao.getAll();

        if (all.size() >= 100) {
            throw new SeriesMaxLimitException();
        }

        long countSameName = all.stream()
                .filter(s -> s.getName().equals(series.getName()))
                .count();
        if (countSameName >= 20) {
            throw new SeriesNameLimitException(series.getName());
        }

        if (all.stream().anyMatch(s -> s.getId() == series.getId())) {
            throw new SeriesDuplicateIdException(series.getId());
        }

        if ("Special".equalsIgnoreCase(series.getName()) &&
                (series.getDescription() == null || series.getDescription().isBlank())) {
            throw new SeriesSpecialDescriptionException();
        }

        if (series.getEpisodes().size() > 50) {
            throw new SeriesMaxEpisodesException();
        }

        this.seriesDao.save(series);
    }

    public void updateSeries(@Valid Series series) throws Exception {
        Series existing = this.seriesDao.get(series.getId());
        if (existing == null) {
            throw new SeriesNotFoundException(series.getId());
        }

        List<Series> all = this.seriesDao.getAll();
        long countSameName = all.stream()
                .filter(s -> !s.equals(series) && s.getName().equals(series.getName()))
                .count();
        if (countSameName >= 20) {
            throw new SeriesNameLimitException(series.getName());
        }

        this.seriesDao.update(series);
    }

    public void deleteSeries(int id) throws Exception {
        Series existing = this.seriesDao.get(id);
        if (existing == null) {
            throw new SeriesNotFoundException(id);
        }
        this.seriesDao.delete(id);
    }

    public Series getSeries(int id) throws Exception {
        Series series = this.seriesDao.get(id);
        if (series == null) {
            throw new SeriesNotFoundException(id);
        }
        return series;
    }
}
