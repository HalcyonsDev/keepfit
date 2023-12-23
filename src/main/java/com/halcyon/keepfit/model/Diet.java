package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "diets")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Diet {
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

    @Column(name = "percentage_of_carbohydrates")
    private String percentageOfCarbohydrates;

    @Column(name = "percentage_of_fats")
    private String percentageOfFats;

    @Column(name = "percentage_of_proteins")
    private String percentageOfProteins;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    private User owner;

    @OneToMany(mappedBy = "diet")
    @JsonBackReference
    private List<Meal> meals;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
