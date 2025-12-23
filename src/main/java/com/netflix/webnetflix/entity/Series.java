package com.netflix.webnetflix.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Series implements Comparable<Series>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    private List<Episode> episodes;

    public Series(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.episodes = new ArrayList<>();
    }

    public void addEpisode(Episode episode) {
        this.episodes.add(episode);
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
