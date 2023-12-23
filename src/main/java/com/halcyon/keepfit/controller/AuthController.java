package com.halcyon.keepfit.controller;

import com.halcyon.keepfit.dto.auth.SignUpDto;
import com.halcyon.keepfit.payload.AuthRequest;
import com.halcyon.keepfit.payload.AuthResponse;
import com.halcyon.keepfit.payload.RefreshRequest;
import com.halcyon.keepfit.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody @Valid SignUpDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        AuthResponse response = authService.signup(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody AuthRequest authRequest) {
        String response = authService.delete(authRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/access")
    public ResponseEntity<AuthResponse> getAccessToken(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse response = authService.getTokenByRefresh(refreshRequest.getRefreshToken(), false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> getRefreshToken(@RequestBody RefreshRequest refreshRequest) {
        AuthResponse response = authService.getTokenByRefresh(refreshRequest.getRefreshToken(), true);
        return ResponseEntity.ok(response);
    }
}
