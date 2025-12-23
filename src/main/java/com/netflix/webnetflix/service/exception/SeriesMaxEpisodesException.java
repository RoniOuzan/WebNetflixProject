package com.netflix.webnetflix.service.exception;

public class SeriesMaxEpisodesException extends SeriesException {
    public SeriesMaxEpisodesException() {
        super("Series cannot have more than 50 episodes");
    }
}