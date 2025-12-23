package com.netflix.webnetflix.service;

import com.netflix.webnetflix.dal.SeriesDao;
import com.netflix.webnetflix.entity.Series;
import com.netflix.webnetflix.service.exception.SeriesException;
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
            throw new SeriesException("Cannot save more than 100 series");
        }

        // max 20 series with same name
        long countSameName = all.stream()
                .filter(s -> s.getName().equals(series.getName()))
                .count();
        if (countSameName >= 20) {
            throw new SeriesException("Cannot add new series with name: " + series.getName());
        }

        // no duplicate ID
        if (all.stream().anyMatch(s -> s.getId() == series.getId())) {
            throw new SeriesException("Series with ID " + series.getId() + " already exists");
        }

        // Description cannot be empty if name is "Special"
        if ("Special".equalsIgnoreCase(series.getName()) && 
            (series.getDescription() == null || series.getDescription().isBlank())) {
            throw new SeriesException("Special series must have a description");
        }

        // Max 50 episodes
        if (series.getEpisodes().size() > 50) {
            throw new SeriesException("Series cannot have more than 50 episodes");
        }

        this.seriesDao.save(series);
    }

    public void updateSeries(@Valid Series series) throws Exception {
        Series existing = this.seriesDao.get(series.getId());
        if (existing == null) {
            throw new SeriesException("Cannot update series with ID " + series.getId() + ". It is not found");
        }

        List<Series> all = this.seriesDao.getAll();

        // max 20 series with same name excluding self
        long countSameName = all.stream()
                .filter(s -> !s.equals(series) && s.getName().equals(series.getName()))
                .count();
        if (countSameName >= 20) {
            throw new SeriesException("Cannot change series name to " + series.getName());
        }

        this.seriesDao.update(series);
    }

    public void deleteSeries(int id) throws Exception {
        Series existing = this.seriesDao.get(id);
        if (existing == null) {
            throw new SeriesException("Series with ID " + id + " was not deleted");
        }
        this.seriesDao.delete(id);
    }

    public Series getSeries(int id) throws Exception {
        return this.seriesDao.get(id);
    }
}
