package com.example.kolekcje.plan_treningowy;

import java.util.List;

public class CwiczeniaPlanuTreningowego {
    int id;
    List<Seria> serie;

    public int getId() {return id; }
    public void setId(int id) {this.id=id;}

    public List<Seria> getSerie() {return serie;}
    public void setSerie(List<Seria> serie) {this.serie=serie;}

}
