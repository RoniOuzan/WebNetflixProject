package com.netflix.webnetflix.dal;

import com.netflix.webnetflix.entity.Series;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class SeriesFileDao implements SeriesDao {

    private static final String FILE_PATH = "./series.dat";

    @Override
    public List<Series> getAll() throws Exception {
        return readFromFile();
    }

    @Override
    public void save(Series series) throws Exception {
        List<Series> seriesList = readFromFile();

        if (!seriesList.contains(series)) {
            seriesList.add(series);
        }

        writeToFile(seriesList);
    }

    @Override
    public void update(Series series) throws Exception {
        List<Series> seriesList = readFromFile();

        for (int i = 0; i < seriesList.size(); i++) {
            if (seriesList.get(i).getId() == series.getId()) {
                seriesList.set(i, series);
                break;
            }
        }

        writeToFile(seriesList);
    }

    @Override
    public void delete(int id) throws Exception {
        List<Series> seriesList = readFromFile();
        seriesList.removeIf(s -> s.getId() == id);
        writeToFile(seriesList);
    }

    @Override
    public Series get(int id) throws Exception {
        List<Series> seriesList = readFromFile();

        for (Series series : seriesList) {
            if (series.getId() == id) {
                return series;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<Series> readFromFile() throws Exception {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        try {
            return (ArrayList<Series>) ois.readObject();
        } finally {
            ois.close();
        }
    }

    private void writeToFile(List<Series> seriesList) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
        try {
            oos.writeObject(seriesList);
        } finally {
            oos.close();
        }
    }
}
