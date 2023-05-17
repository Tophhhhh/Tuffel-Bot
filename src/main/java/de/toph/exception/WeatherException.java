package de.toph.exception;

public class WeatherException extends Exception {

    private static final long serialVersionUID = 1260898900352819375L;

    public WeatherException(String errorMessage) {
	super(errorMessage);
    }
}