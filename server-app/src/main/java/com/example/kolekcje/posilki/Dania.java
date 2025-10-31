package com.example.kolekcje.posilki;

import java.util.List;

public class Dania {
    private int id;
    private String nazwa;
    private List<SpozyteProdukty> listaProdukty;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNazwa() {return nazwa;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

    public List<SpozyteProdukty> getListaProdukty() {return listaProdukty;}
    public void setListaProdukty(List<SpozyteProdukty> listaProdukty) {this.listaProdukty = listaProdukty;}

}
