package com.example.kolekcje.posilki;

public class MealInfo {
    long id;    // id produktu w bazie danych
    String nazwa;
    String producent;
    String kodKreskowy;
    Dawka objetosc;

    public void setId(Long id) {this.id = id;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}
    public void setProducent(String producent) {this.producent = producent;}
    public void setKodKreskowy(String kodKreskowy) {this.kodKreskowy = kodKreskowy;}
    public void setObjetosc(Dawka objetosc) {this.objetosc = objetosc;}

    public long getId() {return id;}
    public String getNazwa() {return nazwa;}
    public String getProducent() {return producent;}
    public String getKodKreskowy() {return kodKreskowy;}
    public Dawka getObjetosc() {return objetosc;}

}
