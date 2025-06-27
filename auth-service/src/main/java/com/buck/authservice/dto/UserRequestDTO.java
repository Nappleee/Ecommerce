package com.buck.authservice.dto;

public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String firstName, String password, String email, String lastName) {
        this.firstName = firstName;
        this.password = password;
        this.email = email;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
