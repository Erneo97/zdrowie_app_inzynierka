package com.example.kolekcje.uzytkownik;

import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.posilki.Dania;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Date;
import java.util.List;


@Document(collection = "Uzytkownik")
public class Uzytkownik {
    @Id
    int id;
    int aktualnyPlan;
    String imie, nazwisko, email, haslo;
    List<PommiarWagii> waga;
    int wzrost, zapotrzebowanieKcal;
    Plec plec;
    boolean blocked;
    String role = "USER";
    Date dataUrodzenia;
    List<Przyjaciele> przyjaciele;
    List<Dania> dania;

    public void setRole(String role) {this.role = role;}
    public String getRole() {return role;}

    public void setBlocked(boolean blocked) {
         this.blocked = blocked;
    }
    public boolean isBlocked() {return blocked; }

    public void setDania(List<Dania> dania) {
        this.dania = dania;
    }
    public List<Dania> getDania() {return this.dania;}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDataUrodzenia() {
        return dataUrodzenia;
    }
    public void setDataUrodzenia(Date dataUrodzenia) {this.dataUrodzenia = dataUrodzenia; }

    public int getZapotrzebowanieKcal() {
        return zapotrzebowanieKcal;
    }

    public void setZapotrzebowanieKcal(int zapotrzebowanieKcal) {
        this.zapotrzebowanieKcal = zapotrzebowanieKcal;
    }

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

    public int getAktualnyPlan() { return aktualnyPlan; }
    public void setAktualnyPlan(int aktualnyPlan) {this.aktualnyPlan = aktualnyPlan; }

    public int getWzrost() { return wzrost; }
    public void setWzrost(int wzrost) { this.wzrost = wzrost; }

    public void setPrzyjaciele(List<Przyjaciele> upowaznieniiDoTablicyPosilkow) { this.przyjaciele = upowaznieniiDoTablicyPosilkow; }
    public List<Przyjaciele> getPrzyjaciele() {return przyjaciele;}

    public void setPlec(Plec plec) { this.plec = plec; }
    public Plec getPlec() { return plec; }
}
