package ru.frontierspb.util.exceptions;

import java.util.Map;

public class BookingNotFoundException
        extends GenericException {
    public BookingNotFoundException() {
        super();
    }

    public BookingNotFoundException(Map<String, String> messages) {
        super(messages);
    }
}
