package com.example.kolekcje.uzytkownik;

import com.example.kolekcje.enumy.Plec;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;


@Document(collection = "Uzytkownik")
public class Uzytkownik {
    @Id
    int id;
    int aktualnyPlan;
    String imie, nazwisko, email, haslo, token;
    List<PommiarWagii> waga;
    int wzrost;
    Plec plec;
    List<Przyjaciele> przyjaciele;

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

    public List<PommiarWagii> getWaga() { return waga; }
    public void setWaga(List<PommiarWagii> waga) { this.waga = waga; }

    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return this.token;
    }

    public int getAktualnyPlan() { return aktualnyPlan; }
    public void setAktualnyPlan(int aktualnyPlan) {this.aktualnyPlan = aktualnyPlan; }

    public boolean isTokenCorrect(String token) {
        return token.equals(this.token);
    }

    public int getWzrost() { return wzrost; }
    public void setWzrost(int wzrost) { this.wzrost = wzrost; }

    public List<Przyjaciele> getUpowaznieniiDoTablicyPosilkow() { return przyjaciele; }
    public void setUpowaznieniiDoTablicyPosilkow(List<Przyjaciele> upowaznieniiDoTablicyPosilkow) { this.przyjaciele = upowaznieniiDoTablicyPosilkow; }

    public void setPlec(Plec plec) { this.plec = plec; }
    public Plec getPlec() { return plec; }
}
