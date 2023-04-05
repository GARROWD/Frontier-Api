package ru.frontierspb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "options")
public class Option {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private float value;
}
