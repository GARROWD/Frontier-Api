package ru.frontierspb.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingRequest {
    private long roomId;

    private LocalDateTime date;

    private int hours;

    private String details;
}
