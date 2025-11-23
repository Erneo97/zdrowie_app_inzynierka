package com.example.kolekcje.uzytkownik;

import com.mongodb.DBObject;
import org.springframework.format.annotation.DateTimeFormat;

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
}
