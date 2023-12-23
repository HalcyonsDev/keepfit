package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.halcyon.keepfit.model.chat.ChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "email")
    private String email;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "about")
    private String about;

    @Column(name = "experience")
    private int experience;

    @Column(name = "rank")
    private Rank rank;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Workout> workouts;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Goal> goals;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Meal> meals;

    @OneToMany(mappedBy = "owner")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Diet> diets;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
