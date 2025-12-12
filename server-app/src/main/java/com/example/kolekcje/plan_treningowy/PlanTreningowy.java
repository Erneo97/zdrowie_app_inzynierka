package com.example.kolekcje.plan_treningowy;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "PlanTreningowy")
public class PlanTreningowy {
    @id
    private int id;
    private int id_uzytkownia;
    Date dataUtworzenia;
    String nazwa;

    public PlanTreningowy() { }
    public int getId() {return id;}
    public int getId_uzytkownia() {return id_uzytkownia;}
    public Date getDataUtworzenia() {return dataUtworzenia;}
    public String getNazwa() {return nazwa;}

    public void setId(int id) {this.id = id;}
    public void setId_uzytkownia(int id_uzytkownia) {this.id_uzytkownia = id_uzytkownia;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}
    public void setDataUtworzenia(Date data) { dataUtworzenia = data; }
}
