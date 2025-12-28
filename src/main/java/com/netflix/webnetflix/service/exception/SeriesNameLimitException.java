package com.netflix.webnetflix.service.exception;

public class SeriesNameLimitException extends SeriesException {
    public SeriesNameLimitException(String name) {
        super("Cannot add new series with name: " + name + ". Max 20 allowed.");
    }
}