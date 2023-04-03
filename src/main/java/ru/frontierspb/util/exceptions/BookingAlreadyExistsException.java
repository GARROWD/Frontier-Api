package ru.frontierspb.util.exceptions;

import java.util.Map;

public class BookingAlreadyExistsException extends GenericException {
    public BookingAlreadyExistsException(Map<String, String> messages) {
        super(messages);
    }
}
