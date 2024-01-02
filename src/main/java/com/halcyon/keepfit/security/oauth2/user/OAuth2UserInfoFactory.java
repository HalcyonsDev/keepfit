package com.halcyon.keepfit.security.oauth2.user;

import com.halcyon.keepfit.exception.OAuth2AuthenticationProcessingException;
import com.halcyon.keepfit.model.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.github.toString())) {
            return new GithubOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.discord.toString())) {
            return new DiscordOAuth2UserInfo(attributes);
        }

        throw new OAuth2AuthenticationProcessingException("Sorry, login with " + registrationId + " is not supported yet.");
    }
}
