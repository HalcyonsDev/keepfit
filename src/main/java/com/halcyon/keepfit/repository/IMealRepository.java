package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.Diet;
import com.halcyon.keepfit.model.Meal;
import com.halcyon.keepfit.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMealRepository extends JpaRepository<Meal, Long> {
    Optional<Meal> findById(Long id);
    List<Meal> findAllByOwner(User owner);
    List<Meal> findAllByDiet(Diet diet);

    @Transactional
    @Modifying
    @Query("UPDATE Meal meal SET meal.title = ?1 WHERE meal.id = ?2")
    void updateTitleById(String title, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Meal meal SET meal.description = ?1")
    void updateDescriptionById(String description, Long id);
}
