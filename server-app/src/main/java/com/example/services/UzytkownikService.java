package com.example.services;

import com.example.kolekcje.Zaproszenie;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.posilki.Dania;
import com.example.kolekcje.uzytkownik.*;
import com.example.repositories.UzytkownikRepository;
import com.example.repositories.ZaproszeniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Component
public class UzytkownikService {

    private final UzytkownikRepository repository;
    private final SequenceGeneratorService sequenceGenerator;
    private final ZaproszeniaRepository zaproszeniaRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private static final Logger log = LoggerFactory.getLogger(UzytkownikService.class);

    public UzytkownikService(UzytkownikRepository repository, SequenceGeneratorService sequenceGenerator, ZaproszeniaRepository zaproszeniaRepository, UzytkownikRepository uzytkownikRepository) {
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
        this.zaproszeniaRepository = zaproszeniaRepository;
        this.uzytkownikRepository = uzytkownikRepository;
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

        List<Dania> dania = new ArrayList<>();
        List<Przyjaciele> przyjaciele = new ArrayList<>();
        List<PommiarWagii> wagi = new ArrayList<>();

        int id = sequenceGenerator.getNextSequence(LicznikiDB.UZYTKOWNICY.getNazwa());
        user.setImie(imie);
        user.setNazwisko(nazwisko);
        user.setEmail(email);
        user.setHaslo(haslo);
        user.setWzrost(wzrost);
        user.setId(id);
        user.setPlec(plec);
        user.setDania(dania);
        user.setPrzyjaciele(przyjaciele);
        user.setWaga(wagi);
        return repository.save(user);
    }

    public Optional<Uzytkownik> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<Uzytkownik> loginUser(String email) {
        return repository.findByEmail(email);
    }

    public Optional<Uzytkownik> updateUser(String email, Uzytkownik updatedUser) {
        return repository.findByEmail(email).map(existingUser -> {
            existingUser.setImie(updatedUser.getImie());
            existingUser.setNazwisko(updatedUser.getNazwisko());
            existingUser.setZapotrzebowanieKcal(updatedUser.getZapotrzebowanieKcal());
            existingUser.setDataUrodzenia(updatedUser.getDataUrodzenia());
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


    public boolean addUserWeights(String email, PommiarWagii noweDane) {
       Optional<Uzytkownik> userOpt = repository.findByEmail(email);
       if( userOpt.isPresent()) {
           Uzytkownik user = userOpt.get();
           List<PommiarWagii> wagii = user.getWaga();

           if(wagii == null || wagii.isEmpty()) {
               wagii = new ArrayList<>();
           }

           wagii.add(noweDane);
           user.setWaga(wagii);

           repository.save(user);
           return true;
       }
       return false;
    }


    public void updateUserPassword(String email, String password) {
        // TODO: hash hasła
        repository.findByEmail(email).ifPresent(u -> {
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


    public boolean isInvitationToFriend(int idUser) {
        List<Zaproszenie> zaproszenies = zaproszeniaRepository.findByIdZapraszanego(idUser);
        return !zaproszenies.isEmpty();
    }

    public void deleteInvitationById(int id) {
        zaproszeniaRepository.deleteById(id);
    }

    public Optional<Zaproszenie> getZaproszenieById(int id) {
        return zaproszeniaRepository.findById(id);
    }


    public boolean sendInvitation(int userId, String emailFriend) {
        Optional<Uzytkownik> firend = getUserByEmail(emailFriend);

        if(firend.isPresent()) {
            Optional<Zaproszenie> optZap = zaproszeniaRepository.findByIdZapraszanegoAndIdZapraszajacego(firend.get().getId(), userId);
            if( optZap.isPresent()) {
                log.info("Powtóka {} {}", optZap.get().getidZapraszajacego(), optZap.get().getidZapraszanego());
                return false;
            }
            if( userId == firend.get().getId())
                return false;

            Zaproszenie zaproszenie = new Zaproszenie();

            zaproszenie.setId(sequenceGenerator.getNextSequence(LicznikiDB.ZAPROSZENIA.getNazwa()));
            zaproszenie.setidZapraszajacego(userId);
            zaproszenie.setidZapraszanego(firend.get().getId());
            log.info("Udało się {} {}", zaproszenie, zaproszenie.getId());
            zaproszeniaRepository.save(zaproszenie);
            return true;
        }

        return false;
    }

}
