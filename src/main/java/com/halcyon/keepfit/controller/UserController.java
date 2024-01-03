package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.service.user.UserService;
import jakarta.validation.constraints.Size;
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

    @PatchMapping("/update-firstname")
    public ResponseEntity<User> updateFirstname(
            @RequestParam
            @Size(min = 2, max = 50, message = "Firstname must be more than 1 character and less than 50 characters.") String value
    ) {
        User user = userService.updateFirstname(value);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update-lastname")
    public ResponseEntity<User> updateLastname(
            @RequestParam
            @Size(min = 2, max = 50, message = "Lastname must be more than 1 character and less than 50 characters.") String value
    ) {
        User user = userService.updateLastname(value);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update-about")
    public ResponseEntity<User> updateAbout(
            @RequestParam
            @Size(max = 64, message = "About must be less than 64 characters.") String value
    ) {
        User user = userService.updateAbout(value);
        return ResponseEntity.ok(user);
    }
}
