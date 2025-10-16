package com.example.kolekcje.uzytkownik;

public class Tokens {
    int id_pytajacego, id_docelowego, id;
    String token;

    public Tokens(int id, int id_pytajacego, int id_docelowego, String token) {
        this.id = id;
        this.id_pytajacego = id_pytajacego;
        this.id_docelowego = id_docelowego;
        this.token = token;
    }
    public int getId() {return id;}
    public int getId_pytajacego() {return id_pytajacego;}
    public int getId_docelowego() {return id_docelowego;}
    public String getToken() {return token;}

}
