package ru.frontierspb.util.exceptions;

import java.util.Map;

public class VerificationException
        extends GenericException {
    public VerificationException(Map<String, String> messages) {
        super(messages);
    }
}