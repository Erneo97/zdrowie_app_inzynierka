package com.example.kolekcje.uzytkownik;

public class Przyjaciele {
    int id;
    boolean czyDozwolony;
    boolean czyPrzyjaciel;

    boolean isCzyDozwolony() {return czyDozwolony;}
    void setCzyDozwolony(boolean czyDozwolony) {this.czyDozwolony = czyDozwolony;}

    public int getId() {return id;}


    boolean isCzyPrzyjaciel() {return czyPrzyjaciel;}
    void setPrzyjaciel(boolean przyjaciel) {this.czyPrzyjaciel = przyjaciel;}
}
