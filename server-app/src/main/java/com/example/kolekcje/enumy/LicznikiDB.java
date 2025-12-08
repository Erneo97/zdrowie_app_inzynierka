package com.example.kolekcje.enumy;

public enum LicznikiDB {
    UZYTKOWNICY("users"),
    PLANY_TRENINGOWE("trening plans"),
    PRODUKTY("products"),
    POSILKI("meals"),
    TRENINGI("trenings"),
    CWICZENIA("cwiczenia"),
    TOKENS("tokens"),
    ZAPROSZENIA("zaproszenia");

    private String nazwa;
    private LicznikiDB(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getNazwa() {return nazwa;}
}
