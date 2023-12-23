package com.halcyon.keepfit.repository;

import com.halcyon.keepfit.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
}
