package ru.frontierspb.util.messages;

import java.util.ResourceBundle;

public class ExceptionsMessages {
    private static final ResourceBundle resourceBundle;

    static {
        resourceBundle = ResourceBundle.getBundle("exceptionsMessages");
    }

    public static String getMessage(String key) {
        return resourceBundle.getString(key);
    }
}