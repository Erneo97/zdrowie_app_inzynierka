package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.uzytkownik.PomiarWagiiProjection;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.UzytkownikRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class UzytkownikService {

    private final UzytkownikRepository repository;
    private final SequenceGeneratorService sequenceGenerator;

    public UzytkownikService(UzytkownikRepository repository, SequenceGeneratorService sequenceGenerator) {
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
    }

    /**
     * Tworzenie nowego użytkownika
     * @param imie
     * @param nazwisko
     * @param email
     * @param haslo
     * @param wzrost
     * @param plec
     * @return
     */
    public Uzytkownik createUser(String imie, String nazwisko, String email, String haslo, int wzrost, Plec plec) {
        Uzytkownik user = new Uzytkownik();
        int id = sequenceGenerator.getNextSequence(LicznikiDB.UZYTKOWNICY.getNazwa());
        user.setImie(imie);
        user.setNazwisko(nazwisko);
        user.setEmail(email + id);
        user.setHaslo(haslo);
        user.setWzrost(wzrost);
        user.setId(id);
        user.setPlec(plec);

        return repository.save(user);
    }

    public Optional<Uzytkownik> getUserById(int id) {
        return repository.findById(id);
    }


    public Optional<Uzytkownik> updateUser(int id, Uzytkownik updatedUser) {
        return repository.findById(id).map(existingUser -> {
            existingUser.setImie(updatedUser.getImie());
            existingUser.setNazwisko(updatedUser.getNazwisko());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setWzrost(updatedUser.getWzrost());
            return repository.save(existingUser);
        });
    }

    public boolean deleteUser(int id) {
        if( repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Pobieranie listy ze wszystkimi pomiarami użytkownika jakie ten wykonał w czasie używania aplikacji
     * @param id
     * @return
     */
    public List<PommiarWagii> getUsersWeights(int id) {
        return repository.findProjectedById(id)
                .map(PomiarWagiiProjection::getDane)
                .orElse(Collections.emptyList());
    }

    /**
     * Aktualizacja listy ze wszystkimi wagami użytkownika
     * @param id
     * @param noweDane
     */
    public void updateUserWeights(int id, List<PommiarWagii> noweDane) {
       repository.findById(id).ifPresent(u -> {
           u.setWaga(noweDane);
           repository.save(u);
       });
    }

    /**
     * Aktualizacja hasla uzytkownika
     * @param id
     * @param password
     */
    public void updateUserPassword(int id, String password) {
        repository.findById(id).ifPresent(u -> {
            u.setHaslo(password);
            repository.save(u);
        });
    }


    /**
     * Zmiana aktualnego planu treniongowego
     * @param id
     * @param planId
     */
    public void updateUserTreningPlan(int id, int planId) {
        repository.findById(id).ifPresent(u -> {
            u.setAktualnyPlan(planId);
            repository.save(u);
        });
    }

}
