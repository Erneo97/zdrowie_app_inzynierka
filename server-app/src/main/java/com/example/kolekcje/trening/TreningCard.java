package com.example.kolekcje.trening;

public class TreningCard {
    private int idTrening, spaloneKcal;
    private String date, nazwa;
    private float spaloneKalorie;
    private int iloscCwiczen;

    public int getIdTrening() {return idTrening;}
    public void setIdTrening(int idTrening) {this.idTrening = idTrening;}

    public int getIloscCwiczen() {return iloscCwiczen;}
    public void setIloscCwiczen(int iloscCwiczen) {this.iloscCwiczen = iloscCwiczen;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public String getNazwa() {return nazwa;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

    public void setSpaloneKalorie(float spaloneKalorie) {this.spaloneKalorie = spaloneKalorie; }
    public float getSpaloneKalorie() {return spaloneKalorie; }
}
