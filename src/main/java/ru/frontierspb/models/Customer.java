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
    @Column(name = "id")
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "points")
    private int points; //francs

    /*
    @OneToMany(mappedBy = "referral", fetch = FetchType.LAZY)
    private List<Referent> referrers;

    @OneToMany(mappedBy = "referrer", fetch = FetchType.LAZY)
    private List<Referent> referrals;
    */

    @Column(name = "role")
    private String role;

    public void accruePoints(int points){
        this.points += points;
    }

    public void deductPoints(int points){
        this.points -= points;
    }

    public Customer(long id){
        this.id = id;
    }
}
