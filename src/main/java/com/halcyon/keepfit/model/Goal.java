package com.halcyon.keepfit.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "goals")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
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

    @Column(name = "status")
    private Status status;

    @Column(name = "complexity")
    private Complexity complexity;

    @Column(name = "deadline")
    private Instant deadline;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @JsonManagedReference
    private User owner;

    @PrePersist
    private void onCreate() {
        createdAt = Instant.now();
    }
}
