package com.example.kolekcje.enumy;


public enum GOAL {
    REDUCE("Redukcja wagi"),
    MUSCLE("Masa mięśniowa"),
    CONST("Utrzymanie wagi");


    private final String label;

    GOAL(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
