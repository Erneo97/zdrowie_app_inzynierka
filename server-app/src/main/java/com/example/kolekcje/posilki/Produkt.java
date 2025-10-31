package com.example.kolekcje.posilki;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "Produkty")
public class Produkt {
    @Id
    private long id;
    private String nazwa, producent;
    private float kcal, bialko, weglowodany, tluszcze, blonnik;
    private String kodKreskowy;
    private Dawka objetosc;

    public void setProducent(String producent) {
        this.producent = producent;
    }
    public String getProducent() {return producent;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public String getNazwa() {return nazwa;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

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

    public Dawka getObjetosc() {
        return objetosc;
    }
    public void setObjetosc(Dawka objetosc) {
        this.objetosc = objetosc;
    }
    public float getWeglowodany() {return weglowodany;}
    public void setWeglowodany(float weglowodany) {this.weglowodany = weglowodany;}

    public float getTluszcze() {return tluszcze;}
    public void setTluszcze(float tluszcze) {
        this.tluszcze = tluszcze;
    }

    public String getKodKreskowy() {return kodKreskowy;}
    public void setKodKreskowy(String kodKreskowy) {this.kodKreskowy = kodKreskowy;}



}
