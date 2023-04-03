package ru.frontierspb.util.exceptions;

import java.util.Map;

public class AdminException
        extends GenericException {
    public AdminException(Map<String, String> messages) {
        super(messages);
    }
}
