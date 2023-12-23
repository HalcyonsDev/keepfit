package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Status;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkoutRepository extends JpaRepository<Workout, Long> {
    Optional<Workout> findById(Long id);
    List<Workout> findAllByOwner(User owner);
    List<Workout> findAllByComplexityAndOwner(Complexity complexity, User owner);
    List<Workout> findAllByStatusAndOwner(Status status, User owner);

    @Transactional
    @Modifying
    @Query("UPDATE Workout workout SET workout.title = ?1 WHERE workout.id = ?2")
    void updateTitleById(String title, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Workout workout SET workout.description = ?1 WHERE workout.id = ?2")
    void updateDescription(String description, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Workout workout SET workout.complexity = ?1 WHERE workout.id = ?2")
    void updateComplexity(Complexity complexity, Long id);
}
