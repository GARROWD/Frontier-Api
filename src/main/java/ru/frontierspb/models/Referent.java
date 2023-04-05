package ru.frontierspb.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.frontierspb.util.enums.ReferentLevel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "referent")
public class Referent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referrer_id", nullable = false)
    private Customer referrer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "referral_id", nullable = false)
    private Customer referral;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private ReferentLevel level;

}
