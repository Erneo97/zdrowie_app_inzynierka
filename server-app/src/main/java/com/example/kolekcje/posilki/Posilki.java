package com.example.kolekcje.posilki;

import com.example.kolekcje.enumy.PoraDnia;
import dev.morphia.annotations.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Meals")
public class Posilki {
    @Id
    private int id;
    private int idUzytkownika;
    Date data;
    private PoraDnia poradnia;
    private List<SpozyteProdukty> produkty;


    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}

    public int getId_uzytkownika() {return idUzytkownika;}
    public void setId_uzytkownika(int id_uzytkownika) {this.idUzytkownika = id_uzytkownika;}

    public Date getData() {return data;}
    public void setData(Date data) {this.data = data;}

    public PoraDnia getPoradnia() {return poradnia;}
    public void setPoradnia(PoraDnia poradnia) {
        this.poradnia = poradnia;
    }

    public List<SpozyteProdukty> getProdukty() {return produkty;}
    public void setProdukty(List<SpozyteProdukty> produkty) {this.produkty = produkty;}

}
