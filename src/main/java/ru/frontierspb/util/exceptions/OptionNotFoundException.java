package ru.frontierspb.util.exceptions;

import java.util.Map;

public class OptionNotFoundException extends GenericException{
    public OptionNotFoundException(Map<String, String> messages) {
        super(messages);
    }
}
