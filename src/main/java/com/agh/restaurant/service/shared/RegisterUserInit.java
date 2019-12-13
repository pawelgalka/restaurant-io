package com.agh.restaurant.service.shared;

public class RegisterUserInit {
    private String username;
    private String password;
    private String email;
    private String role;

    public RegisterUserInit() {
        //for Jackson parsing purposes
    }

    public RegisterUserInit(String username, String password, String email, String role) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override public String toString() {
        return "RegisterUserInit{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
