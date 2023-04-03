package ru.frontierspb.dto.responses;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.frontierspb.util.enums.BookingStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookingResponse {
    private long customerId;

    private long roomId;

    private LocalDateTime requestDate;

    private LocalDateTime date;

    private int hours;

    private String details;

    private BookingStatus status;
}
