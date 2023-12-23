package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    private User owner;

    public Token(String value, User owner) {
        this.value = value;
        this.owner = owner;
    }
}
