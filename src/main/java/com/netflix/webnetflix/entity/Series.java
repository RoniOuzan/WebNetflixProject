package com.netflix.webnetflix.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
public class Series implements Comparable<Series>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String description;
    private List<Episode> episodes;

    public Series(int id, String name, String description, List<Episode> episodes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.episodes = episodes;
    }

    @Override
    public int compareTo(Series o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return this.id == series.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.name, this.description, this.episodes);
    }
}
