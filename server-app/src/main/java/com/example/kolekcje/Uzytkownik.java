package com.example.kolekcje;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@Document(collection = "Uzytkownik")
public class Uzytkownik {
    @Id
    int id;
    String imie, nazwisko, email, haslo;
    ArrayList<PommiarWagii> waga;
    int wzrost;
    ArrayList<Integer> upowaznieniiDoTablicyPosilkow;

    public Uzytkownik() {}


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getHaslo() { return haslo; }
    public void setHaslo(String haslo) { this.haslo = haslo; }

    public ArrayList<PommiarWagii> getWaga() { return waga; }
    public void setWaga(ArrayList<PommiarWagii> waga) { this.waga = waga; }

    public int getWzrost() { return wzrost; }
    public void setWzrost(int wzrost) { this.wzrost = wzrost; }

    public ArrayList<Integer> getUpowaznieniiDoTablicyPosilkow() { return upowaznieniiDoTablicyPosilkow; }
    public void setUpowaznieniiDoTablicyPosilkow(ArrayList<Integer> upowaznieniiDoTablicyPosilkow) { this.upowaznieniiDoTablicyPosilkow = upowaznieniiDoTablicyPosilkow; }
}
