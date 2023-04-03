package ru.frontierspb.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SessionResponse {
    private LocalDateTime inDate;

    private LocalDateTime outDate;
}
