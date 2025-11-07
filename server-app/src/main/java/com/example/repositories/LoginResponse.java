package com.example.repositories;

public class LoginResponse {
    private String token;
    private String email;

    public LoginResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {return token;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public void setToken(String token) {this.token = token;}
}
