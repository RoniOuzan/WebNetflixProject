package com.netflix.webnetflix.service.exception;

public class SeriesDuplicateIdException extends SeriesException {
    public SeriesDuplicateIdException(int id) {
        super("Series with ID " + id + " already exists");
    }
}