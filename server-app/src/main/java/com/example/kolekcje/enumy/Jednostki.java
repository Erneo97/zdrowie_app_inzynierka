package com.example.kolekcje.enumy;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Jednostki {
    LITR("l."),
    GRAM("g."),
    KILOGRAM("kg."),
    SZTUKI("szt."),
    MILILITR("ml.");

    final private String naziv;
    private Jednostki(String naziv) {
        this.naziv = naziv;
    }


}
