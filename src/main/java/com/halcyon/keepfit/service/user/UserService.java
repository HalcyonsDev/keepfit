package com.halcyon.keepfit.service.user;

import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.repository.IUserRepository;
import com.halcyon.keepfit.security.JwtAuthentication;
import com.halcyon.keepfit.service.auth.JwtProvider;
import com.halcyon.keepfit.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtProvider jwtProvider;
    private final IUserRepository userRepository;

    public User create(User user) {
        return userRepository.save(user);
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this id not found."));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with this email not found."));
    }

    public User findCurrentUser() {
        JwtAuthentication jwtAuth = (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(jwtAuth.getEmail());
    }

    public User findByToken(String jwtToken) {
        Claims claims = jwtProvider.extractAccessClaims(jwtToken);
        JwtAuthentication jwtAuth = JwtUtil.getAuthentication(claims);

        return findByEmail(jwtAuth.getEmail());
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
