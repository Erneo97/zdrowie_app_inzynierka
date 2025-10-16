package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.uzytkownik.*;
import com.example.repositories.UzytkownikRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class UzytkownikService {

    private final UzytkownikRepository repository;
    private final SequenceGeneratorService sequenceGenerator;
    private final TokensService tokensService;

    public UzytkownikService(UzytkownikRepository repository, SequenceGeneratorService sequenceGenerator, TokensService tokensService) {
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
        this.tokensService = tokensService;
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
        // TODO: hash hasła + token
        List<Dania> dania = new ArrayList<>();
        List<Przyjaciele> przyjaciele = new ArrayList<>();

        int id = sequenceGenerator.getNextSequence(LicznikiDB.UZYTKOWNICY.getNazwa());
        user.setImie(imie);
        user.setNazwisko(nazwisko);
        user.setEmail(email + id);
        user.setHaslo(haslo);
        user.setWzrost(wzrost);
        user.setId(id);
        user.setPlec(plec);
        user.setDania(dania);
        user.setUpowaznieniiDoTablicyPosilkow(przyjaciele);
        return repository.save(user);
    }

    public Optional<Uzytkownik> getUserById(int id) {
        return repository.findById(id);
    }

    public Optional<Uzytkownik> loginUser(String emian, String password) {
        // TODO: hash hasła
        return repository.findByEmailAndHaslo(emian, password);
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
        // TODO: hash hasła
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

    /**
     * Sprawdza czy Dany użytkoiwnik o idFriend może zmieniać spis posiłów u użytkownika id idUser
     * @param idUser
     * @param idFriend
     * @param tokenUser
     * @return
     */
    public boolean isUserCanChangeMeals(int idUser, int idFriend, String tokenUser) {
        Optional<PrzyjacieleProjection> projectionOpt = repository.findPrzyjacieById(idUser);

        if (projectionOpt.isEmpty()) {
            return false;
        }
        PrzyjacieleProjection projection = projectionOpt.get();
        return projection.getPrzyjaciele().stream().anyMatch(friend -> friend.getId() == idFriend); // TODO token

    }

    public void addFriendAuthorizationToChangeMeals(int idUser, int idFriend, String tokenUser) {
        tokensService.createToken(idUser, idFriend, "TODO: token generowany");
    }

}
