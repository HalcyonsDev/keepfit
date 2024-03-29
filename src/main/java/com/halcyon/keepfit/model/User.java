package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "password")
    private String password;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

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

    @OneToMany(mappedBy = "sender")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<FriendRequest> sentFriendRequests;

    @OneToMany(mappedBy = "recipient")
    @JsonBackReference
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<FriendRequest> receivedFriendRequests;

    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    @JsonIgnoreProperties("friends")
    @JsonManagedReference
    private List<User> friends;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
