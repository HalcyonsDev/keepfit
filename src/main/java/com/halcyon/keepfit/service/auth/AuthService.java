package com.halcyon.keepfit.service.auth;

import com.halcyon.keepfit.dto.auth.SignUpDto;
import com.halcyon.keepfit.model.Rank;
import com.halcyon.keepfit.model.Token;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.payload.AuthRequest;
import com.halcyon.keepfit.payload.AuthResponse;
import com.halcyon.keepfit.security.JwtAuthentication;
import com.halcyon.keepfit.service.token.TokenService;
import com.halcyon.keepfit.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignUpDto dto) {
        if (userService.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .about(dto.getAbout())
                .experience(0)
                .rank(Rank.BEGINNER)
                .build();

        userService.create(user);

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(refreshToken, user));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(refreshToken, user));

        return new AuthResponse(accessToken, refreshToken);
    }

    public String delete(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if (!passwordEncoder.matches(user.getPassword(), authRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        userService.delete(user);
        return "User was deleted successfully.";
    }

    public AuthResponse getTokenByRefresh(String refreshToken, boolean isRefresh) {
        String subject = jwtProvider.extractRefreshClaims(refreshToken).getSubject();

        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is not valid.");
        }

        Token token = tokenService.findByValue(refreshToken);

        if (!token.getValue().equals(refreshToken) || !token.getOwner().getEmail().equals(subject)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        User user = userService.findByEmail(subject);

        String accessToken = jwtProvider.generateToken(user, false);
        String newRefreshToken = null;

        if (isRefresh) {
            newRefreshToken = jwtProvider.generateToken(user, true);
            tokenService.create(new Token(newRefreshToken, user));
        }

        return new AuthResponse(accessToken, newRefreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}