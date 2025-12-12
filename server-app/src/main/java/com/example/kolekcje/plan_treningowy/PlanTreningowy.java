package com.example.kolekcje.plan_treningowy;

import com.example.kolekcje.enumy.GOAL;
import dev.morphia.annotations.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "PlanTreningowy")
public class PlanTreningowy {
    @Id
    private int id;
    private int id_uzytkownia;
    Date dataUtworzenia;
    String nazwa;
    List<CwiczeniaPlanuTreningowego> cwiczeniaPlanuTreningowe;
    GOAL cel;

    public GOAL getCel() { return cel; }
    public void setCel(GOAL goal) {this.cel = goal;}

    public PlanTreningowy() { }
    public int getId() {return id;}
    public int getId_uzytkownia() {return id_uzytkownia;}
    public Date getDataUtworzenia() {return dataUtworzenia;}
    public String getNazwa() {return nazwa;}

    public void setId(int id) {this.id = id;}
    public void setId_uzytkownia(int id_uzytkownia) {this.id_uzytkownia = id_uzytkownia;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}
    public void setDataUtworzenia(Date data) { dataUtworzenia = data; }

    public List<CwiczeniaPlanuTreningowego> getCwiczeniaPlanuTreningowe() {return cwiczeniaPlanuTreningowe;}
    public void setCwiczeniaPlanuTreningowe(List<CwiczeniaPlanuTreningowego> cwiczeniaPlanuTreningowe) {this.cwiczeniaPlanuTreningowe = cwiczeniaPlanuTreningowe;}
}
