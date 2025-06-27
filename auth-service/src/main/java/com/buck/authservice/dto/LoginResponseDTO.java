package com.buck.authservice.dto;

public class LoginResponseDTO {
    private String email;
    private String token;
    private String role;
    private String refreshToken;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String email, String token, String role, String refreshToken) {
        this.email = email;
        this.token = token;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
