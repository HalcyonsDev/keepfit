package com.halcyon.keepfit.security.oauth2.user;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
