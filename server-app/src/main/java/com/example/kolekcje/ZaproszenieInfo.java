package com.example.kolekcje;

public class ZaproszenieInfo {
    int id;
    String imie, nazwisko, email;
    boolean creator;

    public ZaproszenieInfo(String imie, String nazwisko, String email, int id, boolean creator) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.id = id;
        this.creator = creator;
    }
    public String getImie() {return imie;}
    public String getNazwisko() {return nazwisko;}
    public String getEmail() {return email;}
    public int getId() {return id;}
    public boolean isCreator() {return creator;}

    public void  setCreator(boolean creator) {this.creator = creator;}
    public void setId(int id) {this.id = id;}
    public void setImie(String imie) {this.imie = imie;}
    public void setNazwisko(String nazwisko) {this.nazwisko = nazwisko;}
    public void setEmail(String email) {this.email = email;}
}
