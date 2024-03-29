package com.halcyon.keepfit.security.oauth2.user;

import java.util.Map;

public class DiscordOAuth2UserInfo extends OAuth2UserInfo {
    public DiscordOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return attributes.get("username").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getImageUrl() {
        return attributes.get("avatar").toString();
    }
}
