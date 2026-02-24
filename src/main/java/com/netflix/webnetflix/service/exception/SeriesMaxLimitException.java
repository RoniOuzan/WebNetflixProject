package com.netflix.webnetflix.service.exception;

public class SeriesMaxLimitException extends SeriesException {
    public SeriesMaxLimitException(int limit) {
        super("Cannot save more than " + limit + " series");
    }
}