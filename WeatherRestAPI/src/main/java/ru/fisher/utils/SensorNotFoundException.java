package ru.fisher.utils;

public class SensorNotFoundException extends RuntimeException {

    public SensorNotFoundException (String message) {
        super(message);
    }
}
