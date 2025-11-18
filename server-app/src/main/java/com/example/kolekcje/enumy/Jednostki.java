package com.example.kolekcje.enumy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Jednostki {
    LITR("l."),
    GRAM("g."),
    KILOGRAM("kg."),
    SZTUKI("szt."),
    MILILITR("ml.");

    private final String displayName;

    Jednostki(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Jednostki fromName(String name) {
        return Jednostki.valueOf(name);
    }
}
