package com.netflix.webnetflix.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
public class Episode implements Comparable<Episode>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private int episodeNumber;

    public Episode(int id, int episodeNumber, String name, String description) {
        this.id = id;
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.description = description;
    }

    @Override
    public int compareTo(Episode o) {
        return Integer.compare(this.episodeNumber, o.episodeNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return this.id == episode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.episodeNumber, this.name, this.description);
    }
}
