package ru.frontierspb.util.exceptions;

import java.util.Map;

public class OptionValidationException
        extends GenericException {
    public OptionValidationException(Map<String, String> messages) {
        super(messages);
    }
}