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
    private int idUzytkownia;
    Date dataUtworzenia;
    String nazwa;
    List<CwiczeniaPlanuTreningowego> cwiczeniaPlanuTreningowe;
    GOAL cel;

    public GOAL getCel() { return cel; }
    public void setCel(GOAL goal) {this.cel = goal;}

    public PlanTreningowy() { }
    public int getId() {return id;}
    public int getIdUzytkownia() {return idUzytkownia;}
    public String getNazwa() {return nazwa;}

    public void setId(int id) {this.id = id;}
    public void setIdUzytkownia(int id_uzytkownia) {this.idUzytkownia = id_uzytkownia;}
    public void setNazwa(String nazwa) {this.nazwa = nazwa;}

    public List<CwiczeniaPlanuTreningowego> getCwiczeniaPlanuTreningowe() {return cwiczeniaPlanuTreningowe;}
    public void setCwiczeniaPlanuTreningowe(List<CwiczeniaPlanuTreningowego> cwiczeniaPlanuTreningowe) {this.cwiczeniaPlanuTreningowe = cwiczeniaPlanuTreningowe;}

    public Date getDataUtworzenia() {
        return dataUtworzenia;
    }

    public void setDataUtworzenia(Date dataUtworzenia) {
        this.dataUtworzenia = dataUtworzenia;
    }
}
