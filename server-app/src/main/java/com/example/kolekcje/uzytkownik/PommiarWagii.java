package com.example.kolekcje.uzytkownik;

import java.util.Date;


public class PommiarWagii {
    public final int  wartosc;
    public final Date data;
    public final int tluszcz, miesnie, nawodnienie;

    public PommiarWagii(int wartosc, Date data, int tluszcz, int miesnie, int nawodnienie) {
        this.wartosc = wartosc;
        this.data = data;
        this.tluszcz = tluszcz;
        this.miesnie = miesnie;
        this.nawodnienie = nawodnienie;
    }
}
