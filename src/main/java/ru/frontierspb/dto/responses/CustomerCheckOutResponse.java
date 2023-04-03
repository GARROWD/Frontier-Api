package ru.frontierspb.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckOutResponse {
    private String customerUsername;

    private LocalDateTime inDate;

    private LocalDateTime outDate;

    private float price;

    private int pointsSpent;
}
