package ru.frontierspb.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCheckInResponse {
    private String customerUsername;

    private LocalDateTime inDate;
}
