package ru.frontierspb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = true, nullable = false)
    private String password;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "points", nullable = false)
    private int points; //francs

    /*
    @OneToMany(mappedBy = "referral", fetch = FetchType.LAZY)
    private List<Referent> referrers;

    @OneToMany(mappedBy = "referrer", fetch = FetchType.LAZY)
    private List<Referent> referrals;
    */

    @Column(name = "role", nullable = false)
    private String role;

    public void incrementPoints(int points){
        this.points += points;
    }

    public void decrementPoints(int points){
        this.points -= points;
    }
}
