package com.example.auth;

public class JwtResponse {
    private String token;
    private int id;

    public JwtResponse(String token, int id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
}
