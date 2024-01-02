package com.halcyon.keepfit.security.oauth2;

import com.halcyon.keepfit.config.AppProperties;
import com.halcyon.keepfit.exception.BadRequestException;
import com.halcyon.keepfit.model.Token;
import com.halcyon.keepfit.model.User;
import com.halcyon.keepfit.security.UserPrincipal;
import com.halcyon.keepfit.service.auth.JwtProvider;
import com.halcyon.keepfit.service.token.TokenService;
import com.halcyon.keepfit.service.user.UserService;
import com.halcyon.keepfit.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.halcyon.keepfit.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenService tokenService;
    private final AppProperties appProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) return;

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry, we're got an unauthorized redirect uri and can't proceed with the authentication.");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userService.findById(userPrincipal.getId());

        String token = jwtProvider.generateToken(user, false);
        tokenService.create(new Token(token, user));



        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedUri = URI.create(authorizedRedirectUri);
                    return authorizedUri.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedUri.getPort() == clientRedirectUri.getPort();
                });
    }
}
