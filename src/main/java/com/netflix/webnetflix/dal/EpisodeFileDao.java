package com.netflix.webnetflix.dal;

import com.netflix.webnetflix.entity.Episode;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@SuppressWarnings("TryFinallyCanBeTryWithResources")
public class EpisodeFileDao implements EpisodeDao {
    private static final String FILE_PATH = "./episode.dat";

    @Override
    public List<Episode> getAll() throws Exception {
        return readFromFile();
    }

    @Override
    public void save(Episode episode) throws Exception{
        List<Episode> episodesList = readFromFile();

        if (!episodesList.contains(episode)) {
            episodesList.add(episode);
        }

        writeToFile(episodesList);
    }
    @Override
    public void update(Episode episode) throws Exception {
        List<Episode> episodeList = readFromFile();

        for (int i = 0; i < episodeList.size(); i++) {
            if (episodeList.get(i).getId() == episode.getId()) {
                episodeList.set(i, episode);
                break;
            }
        }

        writeToFile(episodeList);
    }

    @Override
    public void delete(int id) throws Exception {
        List<Episode> episodeList = readFromFile();
        episodeList.removeIf(s -> s.getId() == id);
        writeToFile(episodeList);
    }

    @Override
    public Episode get(int id) throws Exception {
        List<Episode> episodeList = readFromFile();

        for (Episode episode : episodeList) {
            if (episode.getId() == id) {
                return episode;
            }
        }
        return null;
    }

    private void writeToFile(List<Episode> episodeList) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
        try {
            oos.writeObject(episodeList);
        } finally {
            oos.close();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Episode> readFromFile() throws Exception {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        try {
            return (ArrayList<Episode>) ois.readObject();
        } finally {
            ois.close();
        }
    }
}
