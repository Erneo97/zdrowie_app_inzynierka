package com.example.kolekcje.enumy;

public enum Jednostki {
    LITR("l."),
    GRAM("g."),
    KILOGRAM("kg."),
    SZTUKI("szt.");

    private String naziv;
    private Jednostki(String naziv) {
        this.naziv = naziv;
    }
}
