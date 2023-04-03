package ru.frontierspb.util.exceptions;

import java.util.Map;

public class SessionException
        extends GenericException {
    public SessionException(Map<String, String> messages) {
        super(messages);
    }
}
