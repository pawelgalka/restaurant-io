package com.agh.restaurant.service.shared;

public class RegisterUserInit {
    private final String displayName;
    private final String password;
    private final String email;
    private final String role;

    public RegisterUserInit(String displayName, String password, String email, String role) {
        super();
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
