package com.example.kolekcje.trening;

import com.example.kolekcje.plan_treningowy.Seria;

import java.util.List;

public class CwiczenieWTreningu {
    int id; // id ćwiczenia w bazie
    String nazwa, czas; //  czas w formacie mm:ss
    List<Seria> serie;

    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public String getNazwa() {return nazwa;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

    public String getCzas() {return czas;}
    public void setCzas(String czas) {this.czas = czas;}

    public List<Seria> getSerie() {return serie;}
    public void setSerie(List<Seria> serie) {this.serie = serie;}
}
