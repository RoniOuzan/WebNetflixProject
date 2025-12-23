package com.netflix.webnetflix.service.exception;

public class SeriesNotFoundException extends SeriesException {
    public SeriesNotFoundException(int id) {
        super("Series with ID " + id + " was not found");
    }
}