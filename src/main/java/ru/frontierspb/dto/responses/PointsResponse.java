package ru.frontierspb.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.frontierspb.util.enums.TransactionType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointsResponse {
    private LocalDateTime date;

    private TransactionType type;

    private int points;
}
