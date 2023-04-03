package ru.frontierspb.util.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GenericException extends Exception{
    private Map<String, String> messages;
}
