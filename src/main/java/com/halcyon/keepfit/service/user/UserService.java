package com.halcyon.keepfit.service.user;

import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {
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

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
