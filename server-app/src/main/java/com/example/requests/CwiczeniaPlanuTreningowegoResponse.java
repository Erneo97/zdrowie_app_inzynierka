package com.example.requests;

import com.example.kolekcje.enumy.GrupaMiesniowa;
import com.example.kolekcje.plan_treningowy.Seria;

import java.util.List;

public class CwiczeniaPlanuTreningowegoResponse {
    int id;
    List<Seria> serie;
    String nazwa;
    List<GrupaMiesniowa> grupaMiesniowas;

    public int getId() {return id; }
    public void setId(int id) {this.id=id;}

    public List<Seria> getSerie() {return serie;}
    public void setSerie(List<Seria> serie) {this.serie=serie;}

    public String getNazwa() {return nazwa;}
    public void setNazwa(String nazwa) {this.nazwa=nazwa;}

    public List<GrupaMiesniowa> getGrupoMiesniowas() {return grupaMiesniowas;}
    public void setGrupaMiesniowas(List<GrupaMiesniowa> grupaMiesniowas) {this.grupaMiesniowas=grupaMiesniowas;}

}
