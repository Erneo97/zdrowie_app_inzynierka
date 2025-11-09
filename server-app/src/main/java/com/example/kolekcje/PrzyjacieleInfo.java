package com.example.kolekcje;

public class PrzyjacieleInfo {
    int id;
    boolean czyDozwolony;
    String imie, nazwisko, email;

    public PrzyjacieleInfo(int id, String imie, String nazwisko, String email, boolean czyDozwolony) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.czyDozwolony = czyDozwolony;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImie() {
        return imie;
    }
    public void setImie(String imie) {
        this.imie = imie;
    }
    public String getNazwisko() {
        return nazwisko;
    }
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isCzyDozwolony() {
        return czyDozwolony;
    }
    public void setCzyDozwolony(boolean czyDozwolony) {
        this.czyDozwolony = czyDozwolony;
    }

}
