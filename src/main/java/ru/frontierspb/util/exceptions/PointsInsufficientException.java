package ru.frontierspb.util.exceptions;

import java.util.Map;

public class PointsInsufficientException
        extends GenericException {
    public PointsInsufficientException(Map<String, String> messages) {
        super(messages);
    }
}
