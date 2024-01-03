package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.firstname = ?1 WHERE user.id = ?2")
    void updateFirstnameById(String firstname, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.lastname = ?1 WHERE user.id = ?2")
    void updateLastnameById(String lastname, Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.about = ?1 WHERE user.id = ?2")
    void updateAboutById(String about, Long id);
}
