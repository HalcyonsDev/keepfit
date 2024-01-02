package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = userService.findCurrentUser();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/")
    public ResponseEntity<User> getUserByToken(@RequestParam String token) {
        User user = userService.findByToken(token);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getById(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }
}
