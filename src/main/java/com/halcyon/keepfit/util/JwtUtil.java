package com.halcyon.keepfit.util;

import com.halcyon.keepfit.payload.JwtAuthentication;
import io.jsonwebtoken.Claims;

public class JwtUtil {
    public static JwtAuthentication getAuthentication(Claims claims) {
        JwtAuthentication jwtAuthInfo = new JwtAuthentication();
        jwtAuthInfo.setEmail(claims.getSubject());

        return jwtAuthInfo;
    }
}
