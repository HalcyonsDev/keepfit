package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.Complexity;
import com.halcyon.keepfit.model.Goal;
import com.halcyon.keepfit.model.Status;
import com.halcyon.keepfit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGoalRepository extends JpaRepository<Goal, Long> {
    Optional<Goal> findById(Long id);
    List<Goal> findAllByOwner(User owner);
    List<Goal> findAllByComplexityAndOwner(Complexity complexity, User owner);
    List<Goal> findAllByStatusAndOwner(Status status, User owner);

    @Transactional
    @Modifying
    @Query("UPDATE Goal goal SET goal.title = ?1 WHERE goal.id = ?2")
    void updateTitleById(String title, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Goal goal SET goal.description = ?1 WHERE goal.id = ?2")
    void updateDescriptionById(String description, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Goal goal SET goal.complexity = ?1 WHERE goal.id = ?2")
    void updateComplexityById(Complexity complexity, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Goal goal SET goal.status = ?1 WHERE goal.id = ?2")
    void updateStatusById(Status status, Long id);
}
