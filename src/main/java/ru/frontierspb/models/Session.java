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
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "customer_username")
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
