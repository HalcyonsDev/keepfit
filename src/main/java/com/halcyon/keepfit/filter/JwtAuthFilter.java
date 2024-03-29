package com.halcyon.keepfit.filter;

import com.halcyon.keepfit.security.JwtAuthentication;
import com.halcyon.keepfit.service.auth.JwtProvider;
import com.halcyon.keepfit.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getTokenFromRequest(request);

        if (jwtToken != null && jwtProvider.isValidAccessToken(jwtToken)) {
            Claims claims = jwtProvider.extractAccessClaims(jwtToken);

            JwtAuthentication jwtAuth = JwtUtil.getAuthentication(claims);
            jwtAuth.setAuthenticated(true);

            SecurityContextHolder.getContext().setAuthentication(jwtAuth);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
