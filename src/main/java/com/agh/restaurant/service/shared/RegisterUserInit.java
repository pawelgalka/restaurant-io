package com.agh.restaurant.service.shared;

public class RegisterUserInit {
    private final String userName;
    private final String email;
    private final String role;

    public RegisterUserInit(String userName, String email, String role) {
        super();
        this.userName = userName;
        this.email = email;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
