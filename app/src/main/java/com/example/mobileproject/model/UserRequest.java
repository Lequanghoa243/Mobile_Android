package com.example.mobileproject.model;

public class UserRequest {
    private String userId;

    public UserRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
