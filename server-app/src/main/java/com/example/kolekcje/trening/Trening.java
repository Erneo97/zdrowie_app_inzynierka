package com.example.kolekcje.trening;

import dev.morphia.annotations.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Treningi")
public class Trening {
    @Id
    int idTrening;
    int idUser, idPlanu;
    Date data;
    List<CwiczenieWTreningu> cwiczenia;
    float spaloneKalorie;

    public void setIdTrening(int idTrening) {this.idTrening = idTrening;}
    public void setIdUser(int idUser) {this.idUser = idUser;}
    public void setIdPlanu(int idPlanu) {this.idPlanu = idPlanu;}
    public void setData(Date data) {this.data = data;}

    public void setCwiczenia(List<CwiczenieWTreningu> cwiczenia) {
        this.cwiczenia = cwiczenia;
    }
    public void setSpaloneKalorie(float spaloneKalorie) {this.spaloneKalorie = spaloneKalorie;}

    public int getIdTrening() {return idTrening;}
    public int getIdUser() {return idUser;}
    public int getIdPlanu() {return idPlanu;}
    public Date getData() {return data;}

    public List<CwiczenieWTreningu> getCwiczenia() { return cwiczenia; }
    public float getSpaloneKalorie() {return spaloneKalorie;}
}
