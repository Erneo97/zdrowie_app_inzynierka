package com.example.kolekcje.uzytkownik;

public class Przyjaciele {
    int id;
    String token;
    boolean czyDozwolony;

    boolean getCzyDozwolony() {return czyDozwolony;}
    void setCzyDozwolony(boolean czyDozwolony) {this.czyDozwolony = czyDozwolony;}

    int getId() {return id;}

    String getToken() {return token;}
    void setToken(String token) {this.token = token;}
}
