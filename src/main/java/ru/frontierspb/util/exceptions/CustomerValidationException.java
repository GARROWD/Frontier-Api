package ru.frontierspb.util.exceptions;

import java.util.Map;

public class CustomerValidationException
        extends GenericException {
    public CustomerValidationException(Map<String, String> messages) {
        super(messages);
    }
}