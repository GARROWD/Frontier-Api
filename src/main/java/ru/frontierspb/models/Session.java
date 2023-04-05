package ru.frontierspb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "customer_username", nullable = false)
    private String customerUsername;

    @Column(name = "in_date")
    private LocalDateTime inDate;

    @Column(name = "out_date")
    private LocalDateTime outDate;

    @Column(name = "price")
    private float price;

    @Column(name = "points_spent")
    private int pointsSpent;
}
