package com.example.kolekcje.uzytkownik;

import com.example.kolekcje.enumy.Plec;

public class UserCard {
    private int id;
    private String imie;
    private String nazwisko;
    private String email;
    private Plec plec;
    private String role;
    private int failureCount;
    private boolean blocked;

    public UserCard() {
        this.id = -1;
        this.role = "";
        this.failureCount = 0;
        this.blocked = false;
    }

    public UserCard(
            int id,
            String imie,
            String nazwisko,
            String email,
            Plec plec,
            String role,
            int failureCount,
            boolean blocked
    ) {
        this.id = id;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.plec = plec;
        this.role = role;
        this.failureCount = failureCount;
        this.blocked = blocked;
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

    public Plec getPlec() {
        return plec;
    }

    public void setPlec(Plec plec) {
        this.plec = plec;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
