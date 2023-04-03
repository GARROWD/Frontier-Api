package ru.frontierspb.util.exceptions;

import java.util.Map;

public class IllegalArgumentException extends GenericException{
    public IllegalArgumentException(Map<String, String> messages) {
        super(messages);
    }
}
