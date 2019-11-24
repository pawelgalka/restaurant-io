package com.agh.restaurant.service.shared;

public class RegisterUserInit {
    private String displayName;
    private String password;
    private String email;
    private String role;

    public RegisterUserInit() {
        //for Jackson parsing purposes
    }

    public RegisterUserInit(String displayName, String password, String email, String role) {
        this.password = password;
        this.displayName = displayName;
        this.email = email;
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
