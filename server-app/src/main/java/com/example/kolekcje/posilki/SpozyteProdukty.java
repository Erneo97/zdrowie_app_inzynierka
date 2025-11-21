package com.example.kolekcje.posilki;

public class SpozyteProdukty {
    private int id_produktu;
    private Dawka wartosc;

    public Dawka getWartosc() {
        return wartosc;
    }
    public void setWartosc(Dawka wartosc) {this.wartosc = wartosc;}

    public int getId_produktu() {return id_produktu;}
    public void setId_produktu(int id_produktu) {this.id_produktu = id_produktu;}

}
