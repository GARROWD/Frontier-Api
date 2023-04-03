package ru.frontierspb.util.messages;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import java.util.Map;

@Data
public class Message {
    @JsonUnwrapped
    private final Map<String, String> message;
    /* TODO Профилирование! Docker! Просто, чтобы запомнить, что такое есть. И про @JsonUnwrapped тоже */
}
