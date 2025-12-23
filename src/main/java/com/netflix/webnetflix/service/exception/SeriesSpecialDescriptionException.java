package com.netflix.webnetflix.service.exception;

public class SeriesSpecialDescriptionException extends SeriesException {
    public SeriesSpecialDescriptionException() {
        super("Special series must have a description");
    }
}