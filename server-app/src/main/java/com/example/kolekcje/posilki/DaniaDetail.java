package com.example.kolekcje.posilki;

import java.util.List;

/**
 * Struktura wysyłana do uzytkownika.
 */
public class DaniaDetail {
    int id;
    String nazwa;
    List<Produkt> listaProdukty;

    public DaniaDetail(int id, String nazwa, List<Produkt> listaProdukty) {
        this.id = id;
        this.nazwa = nazwa;
        this.listaProdukty = listaProdukty;
    }

    public int getId() {return id;}
    public String getNazwa() {return nazwa;}
    public List<Produkt> getListaProdukty() {return listaProdukty;}

    public void setId(int id) {this.id = id;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}
    public void setListaProdukty(List<Produkt> listaProdukty) {this.listaProdukty = listaProdukty;}
}
