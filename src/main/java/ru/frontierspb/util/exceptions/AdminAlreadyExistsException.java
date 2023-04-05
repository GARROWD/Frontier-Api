package ru.frontierspb.util.exceptions;

import java.util.Map;

public class AdminAlreadyExistsException
        extends GenericException {
    public AdminAlreadyExistsException(Map<String, String> messages) {
        super(messages);
    }
}
