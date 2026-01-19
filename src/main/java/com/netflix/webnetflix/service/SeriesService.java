package com.netflix.webnetflix.service;

import com.netflix.webnetflix.dal.SeriesDao;
import com.netflix.webnetflix.entity.Series;
import com.netflix.webnetflix.service.exception.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SeriesService {

    private final SeriesDao seriesDao;
    private final int seriesLimit;
    private final int seriesLimitWithSameName;
    private final ValidationService validationService;

    @Autowired
    public SeriesService(SeriesDao seriesDao,
                         @Value("${seriesService.seriesLimit}") int seriesLimit,
                         @Value("${seriesService.seriesLimitWithSameName}") int seriesLimitWithSameName,
                         ValidationService validationService) {
        this.seriesDao = seriesDao;
        this.seriesLimit = seriesLimit;
        this.seriesLimitWithSameName = seriesLimitWithSameName;
        this.validationService = validationService;
    }

    public List<Series> getAllSeries() throws Exception {
        List<Series> list = this.seriesDao.getAll();
        Collections.sort(list);
        return list;
    }

    public void saveSeries(@Valid Series series) throws Exception {
        List<Series> all = this.seriesDao.getAll();

        validationService.validate(series);

        if (all.size() >= this.seriesLimit) {
            throw new SeriesMaxLimitException();
        }

        long countSameName = all.stream()
                .filter(s -> s.getName().equals(series.getName()))
                .count();
        if (countSameName >= this.seriesLimitWithSameName) {
            throw new SeriesNameLimitException(series.getName());
        }

        if (all.stream().anyMatch(s -> s.getId() == series.getId())) {
            throw new SeriesDuplicateIdException(series.getId());
        }

        if ("Special".equalsIgnoreCase(series.getName()) &&
                (series.getDescription() == null || series.getDescription().isBlank())) {
            throw new SeriesSpecialDescriptionException();
        }

        this.seriesDao.save(series);
    }

    public void updateSeries(@Valid Series series) throws Exception {
        validationService.validate(series);

        Series existing = this.seriesDao.get(series.getId());
        if (existing == null) {
            throw new SeriesNotFoundException(series.getId());
        }

        List<Series> all = this.seriesDao.getAll();
        long countSameName = all.stream()
                .filter(s -> !s.equals(series) && s.getName().equals(series.getName()))
                .count();
        if (countSameName >= this.seriesLimitWithSameName) {
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

    @PostConstruct
    public void onStartup() {
        try {
            List<Series> allSeries = seriesDao.getAll();
            System.out.println("=== Application startup ===");
            System.out.println("Series list on startup:");
            allSeries.forEach(s -> System.out.println(s.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void onShutdown() {
        try {
            List<Series> allSeries = seriesDao.getAll();
            System.out.println("=== Application shutdown ===");
            System.out.println("Series list on shutdown:");
            allSeries.forEach(s -> System.out.println(s.getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
