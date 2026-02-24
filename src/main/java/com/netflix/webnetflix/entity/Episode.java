package com.netflix.webnetflix.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Data
public class Episode implements Comparable<Episode>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must be at most 50 characters")
    private String name;

    @Size(max = 200, message = "Description must be at most 200 characters")
    private String description;

    @Min(value = 1, message = "Episode number must be >= 1")
    private int episodeNumber;
    private int seriesId;

    public Episode(int episodeNumber, String name, String description, int seriesId) {
        this.episodeNumber = episodeNumber;
        this.name = name;
        this.description = description;
        this.seriesId = seriesId;
    }

    public Episode() {
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
