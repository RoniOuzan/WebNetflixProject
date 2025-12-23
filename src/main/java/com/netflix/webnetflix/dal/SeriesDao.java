package com.netflix.webnetflix.dal;

import com.netflix.webnetflix.entity.Series;
import jdk.jshell.spi.ExecutionControlProvider;

import java.util.List;

public interface SeriesDao {
    List<Series> getAll() throws Exception;
    void save(Series series) throws Exception;
    void update(Series series) throws Exception;
    void delete(int id) throws Exception;
    Series get(int id) throws Exception;
}
