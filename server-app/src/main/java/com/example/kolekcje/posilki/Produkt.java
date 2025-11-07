package com.example.kolekcje.posilki;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document( collection = "Produkty")
public class Produkt {
    @Id
    private long id;
    private String nazwa, producent;

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



    public Dawka getObjetosc() {
        return objetosc;
    }
    public void setObjetosc(Dawka objetosc) {
        this.objetosc = objetosc;
    }

    public String getKodKreskowy() {return kodKreskowy;}
    public void setKodKreskowy(String kodKreskowy) {this.kodKreskowy = kodKreskowy;}

}
