package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.Diet;
import com.halcyon.keepfit.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IDietRepository extends JpaRepository<Diet, Long> {
    Optional<Diet> findById(Long id);
    List<Diet> findAllByOwner(User owner);

    @Transactional
    @Modifying
    @Query("UPDATE Diet diet SET diet.title = ?1 WHERE diet.id = ?2")
    void updateTitleById(String title, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Diet diet SET diet.description = ?1 WHERE diet.id = ?2")
    void updateDescriptionById(String description, Long id);
}
