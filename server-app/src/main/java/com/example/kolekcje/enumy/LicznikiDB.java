package com.example.kolekcje.enumy;

public enum LicznikiDB {
    UZYTKOWNICY("users"),
    PLANY_TRENINGOWE("trening plans"),
    PRODUKTY("products"),
    POSILKI("meals"),
    TRENINGI("trenings"),
    TOKENS("tokens");

    private String nazwa;
    private LicznikiDB(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {return nazwa;}
}
