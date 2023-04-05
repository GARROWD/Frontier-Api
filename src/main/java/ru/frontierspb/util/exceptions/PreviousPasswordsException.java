package ru.frontierspb.util.exceptions;

import java.util.Map;

public class PreviousPasswordsException
        extends GenericException {
    public PreviousPasswordsException(Map<String, String> messages) {
        super(messages);
    }
}