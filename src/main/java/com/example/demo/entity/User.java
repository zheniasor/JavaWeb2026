package com.example.demo.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class User extends AbstractEntity {
    private int id;

    @NotBlank(message = "Login cannot be empty")
    @Size(min = 3, max = 20, message = "Login must be between {min} and {max} characters")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Login must contain only letters and numbers")
    private String login;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least {min} characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$",
            message = "Password must contain at least one letter and one number")
    private String password;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    private String confirmationToken;
    private boolean confirmed;

    public User() {}

    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.confirmed = false;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getConfirmationToken() { return confirmationToken; }
    public void setConfirmationToken(String confirmationToken) { this.confirmationToken = confirmationToken; }

    public boolean isConfirmed() { return confirmed; }
    public void setConfirmed(boolean confirmed) { this.confirmed = confirmed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login != null ? login.equals(user.login) : user.login == null;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", login='" + login + "', email='" + email + "', confirmed=" + confirmed + "}";
    }
}