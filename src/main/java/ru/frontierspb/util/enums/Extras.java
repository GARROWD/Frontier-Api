package ru.frontierspb.util.enums;

public enum Extras {
    WITH_BAR,
    WITHOUT_BAR;

    public static final String DEFAULT_VALUE = "WITH_BAR"; // Почему WITH_BAR.name() не работает...
}
