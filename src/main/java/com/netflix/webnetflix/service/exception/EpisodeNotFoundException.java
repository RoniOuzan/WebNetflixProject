package com.netflix.webnetflix.service.exception;

public class EpisodeNotFoundException extends RuntimeException {
    public EpisodeNotFoundException(int id) {
        super("Episode with ID " + id + " was not found");
    }
}
