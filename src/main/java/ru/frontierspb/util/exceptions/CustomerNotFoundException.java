package ru.frontierspb.util.exceptions;

import java.util.Map;

public class CustomerNotFoundException
        extends GenericException {
    public CustomerNotFoundException(){
        super();
    }

    public CustomerNotFoundException(Map<String, String> messages) {
        super(messages);
    }
}