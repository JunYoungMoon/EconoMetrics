package com.mjy.econometrics.dto;

import java.util.Map;

public class AuthResponse {
    private Map<String, Object> userAttributes;
    private String jwtToken;

    public AuthResponse(Map<String, Object> userAttributes, String jwtToken) {
        this.userAttributes = userAttributes;
        this.jwtToken = jwtToken;
    }

    // getter 및 setter 메서드
    public Map<String, Object> getUserAttributes() {
        return userAttributes;
    }

    public void setUserAttributes(Map<String, Object> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
