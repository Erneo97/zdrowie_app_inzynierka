package com.example.kolekcje.plan_treningowy;

import com.example.kolekcje.enumy.GrupaMiesniowa;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "Cwiczenia")
public class Cwiczenie {
    @Id
    private int id;
    private String nazwa, opis;
    private List<GrupaMiesniowa> grupaMiesniowas;
    private float met;

    public Cwiczenie() {}

    public int getId() {return id;}
    public String getNazwa() {return nazwa;}
    public String getOpis() {return opis;}
    public List<GrupaMiesniowa> getGrupaMiesniowas() {return grupaMiesniowas;}
    public float getMet() {return met;}

    public void setId(int id) {this.id = id;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}
    public void setOpis(String opis) {this.opis = opis;}
    public void setGrupaMiesniowas(List<GrupaMiesniowa> grupaMiesniowas) {this.grupaMiesniowas = grupaMiesniowas;}
    public void setMet(float met) {this.met = met;}

}
