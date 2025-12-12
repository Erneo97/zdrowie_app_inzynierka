package com.example.kolekcje.plan_treningowy;

public class Seria {
    int liczbaPowtorzen;
    float obciazenie;

    public Seria(int liczbaPowtorzen, float obciazenie) {
        this.liczbaPowtorzen = liczbaPowtorzen;
        this.obciazenie = obciazenie;
    }

    public Seria() {}

    public int getLiczbaPowtorzen() {return liczbaPowtorzen;}
    public float getObciazenie() {return obciazenie;}
    public void setLiczbaPowtorzen(int liczbaPowtorzen) {
        this.liczbaPowtorzen = liczbaPowtorzen;
    }
    public void setObciazenie(float obciazenie) {
        this.obciazenie = obciazenie;
    }

}
