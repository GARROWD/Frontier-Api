package ru.frontierspb.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OptionRequest {
    private String name;

    private float price;
}