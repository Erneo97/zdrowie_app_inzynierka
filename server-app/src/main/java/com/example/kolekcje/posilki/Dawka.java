package com.example.kolekcje.posilki;

import com.example.kolekcje.enumy.Jednostki;

public class Dawka {
    private float wartosc;
    Jednostki jednostki;
    private float kcal, bialko, weglowodany, tluszcze, blonnik;

    public Dawka() {}

    public float getWartosc() {
        return wartosc;
    }
    public void setWartosc(float wartosc) {
        this.wartosc = wartosc;
    }

    public Jednostki getJednostki() {return jednostki;}
    public void setJednostki(Jednostki jednostki) {this.jednostki = jednostki;}


    public float getWeglowodany() {return weglowodany;}
    public void setWeglowodany(float weglowodany) {this.weglowodany = weglowodany;}

    public float getTluszcze() {return tluszcze;}
    public void setTluszcze(float tluszcze) {
        this.tluszcze = tluszcze;
    }
    public float getKcal() {return kcal;}
    public void setKcal(float kcal) {this.kcal = kcal;}

    public float getBialko() {
        return bialko;
    }
    public void setBialko(float bialko) {
        this.bialko = bialko;
    }

    public void setBlonnik(float blonnik) {
        this.blonnik = blonnik;
    }

    public float getBlonnik() {
        return blonnik;
    }
}
