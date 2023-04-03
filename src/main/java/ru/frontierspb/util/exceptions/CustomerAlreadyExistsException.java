package ru.frontierspb.util.exceptions;

import java.util.Map;

public class CustomerAlreadyExistsException
        extends GenericException {
    public CustomerAlreadyExistsException(Map<String, String> messages) {
        super(messages);
    }
}
