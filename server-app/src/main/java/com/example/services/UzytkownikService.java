package com.example.services;

import com.example.kolekcje.Zaproszenie;
import com.example.kolekcje.ZaproszenieInfo;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.posilki.Dania;
import com.example.kolekcje.posilki.DaniaDetail;
import com.example.kolekcje.posilki.Produkt;
import com.example.kolekcje.posilki.SpozyteProdukty;
import com.example.kolekcje.uzytkownik.*;
import com.example.repositories.UzytkownikRepository;
import com.example.repositories.ZaproszeniaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Component
public class UzytkownikService {

    private final UzytkownikRepository repository;
    private final SequenceGeneratorService sequenceGenerator;
    private final ZaproszeniaRepository zaproszeniaRepository;
    private final UzytkownikRepository uzytkownikRepository;
    private final ProduktService produktService;
    private static final Logger log = LoggerFactory.getLogger(UzytkownikService.class);

    public UzytkownikService(UzytkownikRepository repository, SequenceGeneratorService sequenceGenerator
            , ZaproszeniaRepository zaproszeniaRepository
            , UzytkownikRepository uzytkownikRepository
    , ProduktService produktService
    ) {
        this.repository = repository;
        this.sequenceGenerator = sequenceGenerator;
        this.zaproszeniaRepository = zaproszeniaRepository;
        this.uzytkownikRepository = uzytkownikRepository;
        this.produktService = produktService;
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

    public Optional<Uzytkownik> getUserById(int id) {
        return repository.findById(id);
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

    public void updateUser(Uzytkownik updatedUser) {
        uzytkownikRepository.save(updatedUser);
    }

//    public boolean deleteUser(int id) {
//        if( repository.existsById(id)) {
//            repository.deleteById(id);
//            return true;
//        }
//        return false;
//    }


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
        repository.findByEmail(email).ifPresent(u -> {
            u.setHaslo(password);
            repository.save(u);
        });
    }

    private void addFriendToUser(Przyjaciele friend, Uzytkownik user) {
        List<Przyjaciele> list = user.getPrzyjaciele();
        list.add(friend);
        user.setPrzyjaciele(list);
        repository.save(user);
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


    public List<ZaproszenieInfo> getAllZaproszenies(String email) {
        Optional<Uzytkownik> optUser = getUserByEmail(email);
        if(optUser.isPresent()) {
            Uzytkownik user = optUser.get();

            List<Zaproszenie> zaproszenia = zaproszeniaRepository.findByIdZapraszanegoOrIdZapraszajacego(user.getId(), user.getId());
            List<ZaproszenieInfo> zaproszenieInfo = new ArrayList<>();
            zaproszenia.forEach(zaproszenie -> {
                Optional<Uzytkownik> optZapraszajacy = uzytkownikRepository.findById(zaproszenie.getidZapraszajacego());
                Optional<Uzytkownik> optZapraszany = uzytkownikRepository.findById(zaproszenie.getidZapraszanego());
                if( optZapraszajacy.isPresent() && optZapraszany.isPresent()) {
                    Uzytkownik zap = optZapraszajacy.get();
                    Uzytkownik zapra = optZapraszany.get();
                    ZaproszenieInfo zapro;
                    if(user.getId() != zaproszenie.getidZapraszajacego() ) {

                         zapro = new ZaproszenieInfo(zap.getImie(),
                                zap.getNazwisko(),
                                zap.getEmail(),
                                zaproszenie.getId(),
                                false
                        );
                    }
                    else {
                        zapro = new ZaproszenieInfo(zapra.getImie(),
                                zapra.getNazwisko(),
                                zapra.getEmail(),
                                zaproszenie.getId(),
                                true
                        );
                    }

                    zaproszenieInfo.add(zapro);
                }
            });

            return zaproszenieInfo;
        }
        return null;
    }

    private void delInvitation(Zaproszenie zaproszenie) {
        zaproszeniaRepository.delete(zaproszenie);
    }

    public boolean cancelInviotationUser(ZaproszenieInfo zaproszenieInfo) {
        Optional<Zaproszenie> optZap = zaproszeniaRepository.findById(zaproszenieInfo.getId());
        if(optZap.isPresent()) {
            Zaproszenie zaproszenie = optZap.get();

            delInvitation(zaproszenie);
            return true;
        }
        return false;
    }

    public boolean acceptInvitation(Uzytkownik user, ZaproszenieInfo zaproszenieInfo) {
        Optional<Zaproszenie> optZap = zaproszeniaRepository.findById(zaproszenieInfo.getId());
        if(optZap.isPresent()) {
            Zaproszenie zaproszenie = optZap.get();

            Optional<Uzytkownik> optUsr = uzytkownikRepository.findById(zaproszenie.getidZapraszajacego());
            if (optUsr.isPresent()) {
                Przyjaciele przyjaciele1 = new Przyjaciele(user.getId());
                Przyjaciele przyjaciele = new Przyjaciele(zaproszenie.getidZapraszajacego());

                Uzytkownik usr = optUsr.get();

                addFriendToUser(przyjaciele, user);
                addFriendToUser(przyjaciele1, usr);
            }

            delInvitation(zaproszenie);

            return true;
        }
        return false;
    }

    public boolean deleteFriendUser(Uzytkownik user, int id) {
        Optional<Uzytkownik> usr = uzytkownikRepository.findById(id);
        if( usr.isPresent() ) {
            Uzytkownik us = usr.get();

            List<Przyjaciele > lista = user.getPrzyjaciele();
            lista.removeIf(przyjaciele -> przyjaciele.getId() == id);
            user.setPrzyjaciele(lista);
            uzytkownikRepository.save(user);

            lista = us.getPrzyjaciele();
            lista.removeIf(przyjaciele -> usr.get().getId() == id);
            us.setPrzyjaciele(lista);
            uzytkownikRepository.save(us);
            return true;
        }

        return false;
    }

    public boolean changeAccessUserFrend(Uzytkownik user, int id) {
        Optional<Uzytkownik> usr = uzytkownikRepository.findById(id);
        if( usr.isPresent() ) {
            Uzytkownik us = usr.get();

            List<Przyjaciele > lista = user.getPrzyjaciele();
            lista.forEach(przyjaciel -> {
                if( przyjaciel.getId() == id ) {
                    przyjaciel.setCzyDozwolony( !przyjaciel.isCzyDozwolony() );
                }
            });
            user.setPrzyjaciele(lista);
            uzytkownikRepository.save(user);


            return true;
        }

        return false;
    }


    /**
     *Aktualizacja przepisów użytkownika.
     *
     * @param user
     * @param recipes
     */
    public void upgradeUserRecipes(Uzytkownik user, List<DaniaDetail> recipes) {
        AtomicInteger index = new AtomicInteger(0);

        List<Dania> updatedDania = recipes.stream().map( it -> {
            Dania dania = new Dania();
            dania.setId(index.getAndIncrement());
            dania.setNazwa(it.getNazwa());

            List<SpozyteProdukty> sp = new ArrayList<>();
            List<Produkt> produtcs  = it.getListaProdukty();

            produtcs.forEach(produkt -> {
                SpozyteProdukty spozyte = new SpozyteProdukty();

                spozyte.setId((int)produkt.getId());
                spozyte.setWartosc(produkt.getObjetosc().getFirst());

                sp.add(spozyte);
            });


            dania.setListaProdukty(sp);
            return dania;
        }).collect(Collectors.toList());

        user.setDania(updatedDania);
        repository.save(user);
    }

    public List<DaniaDetail> getAllUserRecipes(Uzytkownik user) {
        List<Dania> userDania = user.getDania();
        return userDania.stream().map(it -> {
            List<Produkt> produktyDoWyslania = new ArrayList<>();

            it.getListaProdukty().forEach(sp -> {
                Optional<Produkt> optProd =  produktService.findById(sp.getId());
                if( optProd.isPresent() ) {
                    Produkt prod = optProd.get();
                    prod.setObjetosc(List.of(sp.getWartosc()));
                    produktyDoWyslania.add(prod);

                }
            });


            return new DaniaDetail(it.getId(), it.getNazwa(), produktyDoWyslania);
        }).collect(Collectors.toList());

    }


}
