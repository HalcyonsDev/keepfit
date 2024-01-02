package com.halcyon.keepfit.config;

import com.halcyon.keepfit.filter.JwtAuthFilter;
import com.halcyon.keepfit.security.RestAuthenticationEntryPoint;
import com.halcyon.keepfit.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.halcyon.keepfit.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.halcyon.keepfit.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.halcyon.keepfit.service.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler auth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Disabling
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                // No session will be created or used
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(new RestAuthenticationEntryPoint()))

                .authorizeHttpRequests(
                        auth -> auth
                                // Entry points
                                .requestMatchers(
                                        "/api/auth/**",
                                        "/oauth2/**",
                                        "/login**",
                                        "/webjars/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )

                // OAuth2 settings
                .oauth2Login(customizer -> {
                    customizer.authorizationEndpoint(endpointConfig -> {
                        endpointConfig.baseUri("/oauth2/authorize");
                        endpointConfig.authorizationRequestRepository(cookieAuthorizationRequestRepository());
                    });

                    customizer.redirectionEndpoint(endpointConfig -> {
                        endpointConfig.baseUri("/oauth2/callback/*");
                    });

                    customizer.userInfoEndpoint(endpointConfig -> {
                        endpointConfig.userService(customOAuth2UserService);
                    });

                    customizer.successHandler(oAuth2AuthenticationSuccessHandler);
                    customizer.failureHandler(auth2AuthenticationFailureHandler);
                })

                // Adding jwt filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)

                .build();
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
