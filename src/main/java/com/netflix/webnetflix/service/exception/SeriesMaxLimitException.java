package com.netflix.webnetflix.service.exception;

public class SeriesMaxLimitException extends SeriesException {
    public SeriesMaxLimitException() {
        super("Cannot save more than 100 series");
    }
}