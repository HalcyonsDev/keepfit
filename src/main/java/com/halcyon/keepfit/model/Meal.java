package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "meals")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "recipe")
    private String recipe;

    @Column(name = "carbohydrates")
    private int carbohydrates;

    @Column(name = "fats")
    private int fats;

    @Column(name = "proteins")
    private int proteins;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    private User owner;

    @ManyToOne
    @JoinColumn(name = "diet_id", referencedColumnName = "id")
    @JsonManagedReference
    private Diet diet;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
