package com.example.kolekcje.uzytkownik;


import java.util.Date;


public class PommiarWagii {
    public final Double  wartosc;
    public final Date data;

    public final double tluszcz, miesnie, nawodnienie;

    public PommiarWagii(Double wartosc, Date data, Double tluszcz, Double miesnie, Double nawodnienie) {
        this.wartosc = wartosc;
        this.data = data;
        this.tluszcz = tluszcz;
        this.miesnie = miesnie;
        this.nawodnienie = nawodnienie;
    }

    public double getMiesnie() {
        return miesnie;
    }
    public double getTluszcz() {return tluszcz;}
    public double getWartosc() {return wartosc;}
    public Date getData() {return data;}
    public double getNawodnienie() {return nawodnienie;}
}
