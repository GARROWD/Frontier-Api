package ru.frontierspb.util.exceptions;

import java.util.Map;

public class CustomerReferentException
        extends GenericException{
    public CustomerReferentException(Map<String, String> messages) {
        super(messages);
    }
}
