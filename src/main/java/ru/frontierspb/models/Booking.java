package ru.frontierspb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.frontierspb.util.enums.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "customer_id", nullable = false)
    private long customerId;

    @Column(name = "room_id", nullable = false)
    private long roomId;

    @Column(name = "request_date")
    private LocalDateTime requestDate;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "hours")
    private int hours;

    @Column(name = "details")
    private String details;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
