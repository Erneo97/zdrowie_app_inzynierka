package com.example.services;

import com.example.controllers.ProduktNazwa;
import com.example.kolekcje.enumy.Jednostki;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.posilki.Dawka;
import com.example.kolekcje.posilki.Posilki;
import com.example.kolekcje.posilki.Produkt;
import com.example.kolekcje.posilki.SpozyteProdukty;
import com.example.repositories.MealRepository;
import com.example.repositories.ProduktRepository;
import com.example.requests.MealUpdate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProduktService {
    private final ProduktRepository produktyRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final MealRepository mealRepository;

    public ProduktService(ProduktRepository produktyRepository, SequenceGeneratorService sequenceGenerator, MealRepository mealRepository) {
        this.produktyRepository = produktyRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.mealRepository = mealRepository;
    }

    public Produkt createProducts(String producent, String nazwa, String kodkreskowy, List<Dawka> objetosc) {
        Produkt product;

        Optional<Produkt> Optprodukt = produktyRepository.findByNazwaAndProducent(nazwa, producent);
        if (Optprodukt.isEmpty()) {
            product = new Produkt();
            product.setId(sequenceGenerator.getNextSequence(
                    LicznikiDB.PRODUKTY.getNazwa()
            ));
            product.setProducent(producent);
            product.setNazwa(nazwa);
            product.setKodKreskowy(kodkreskowy);
            product.setObjetosc(objetosc);
            return produktyRepository.save(product);
        }

//         Produkt zostaje zaktualizowany

        product = Optprodukt.get();

        Map<Jednostki, Dawka> istniejące =
                product.getObjetosc().stream()
                        .collect(Collectors.toMap(
                                Dawka::getJednostki,
                                d -> d,
                                (d1, d2) -> d1
                        ));


        for (Dawka nowa : objetosc) {
            Jednostki jednostka = nowa.getJednostki();

            if (istniejące.containsKey(jednostka)) {
                Dawka stara = istniejące.get(jednostka);
                stara.setWartosc(nowa.getWartosc());
                stara.setKcal(nowa.getKcal());
                stara.setBialko(nowa.getBialko());
                stara.setTluszcze(nowa.getTluszcze());
                stara.setWeglowodany(nowa.getWeglowodany());
                stara.setBlonnik(nowa.getBlonnik());
            } else {
                product.getObjetosc().add(nowa);
            }
        }

        return produktyRepository.save(product);
    }

    public Optional<Produkt> findById(int id) {
        return produktyRepository.findById(id);
    }


    public List<Produkt> findByNazwa(String nazwa) {return produktyRepository.findByNazwa(nazwa);}

    /**
     *
     * @param kodKreskowy
     * @return
     */
    public Optional<Produkt> findByKodKreskowy(String kodKreskowy ) {return produktyRepository.findByKodKreskowy(kodKreskowy);}


    public boolean createUserMeal(int userId, MealUpdate meal) {
        Optional<Posilki> optpos =  mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                userId,
                new Date( meal.getData()),
                meal.getPoraDnia());

        if(optpos.isPresent()) {
            return false;
        }

        Posilki posilekNowy = new Posilki();
        posilekNowy.setId(sequenceGenerator.getNextSequence(LicznikiDB.POSILKI.getNazwa()));
        posilekNowy.setId_uzytkownika(userId);
        posilekNowy.setData(new Date(meal.getData()));
        posilekNowy.setPoradnia(meal.getPoraDnia());

        List<SpozyteProdukty> sp = meal.getMeal().stream().map(item -> {SpozyteProdukty nowy = new SpozyteProdukty();
            nowy.setWartosc(item.getObjetosc().getFirst());
            nowy.setId_produktu((int)item.getId());
            return nowy;
        }).toList();

        posilekNowy.setProdukty(sp);

        mealRepository.save(posilekNowy);

        return true;
    }

    public boolean updateUserMeal(int userId, MealUpdate meal) {
        Optional<Posilki> optpos = mealRepository
                .findByIdUzytkownikaAndDataAndPoradnia(
                        userId,
                        new Date(meal.getData()),
                        meal.getPoraDnia()
                );

        if (optpos.isEmpty()) {
            // nie ma takiego posiłku — nie można modyfikować
            return false;
        }

        Posilki istniejacyPosilek = optpos.get();

        List<SpozyteProdukty> noweProdukty = meal.getMeal().stream()
                .map(item -> {
                    SpozyteProdukty nowy = new SpozyteProdukty();
                    nowy.setWartosc(item.getObjetosc().getFirst());
                    nowy.setId_produktu((int) item.getId());
                    return nowy;
                })
                .toList();


        istniejacyPosilek.setProdukty(noweProdukty);
        mealRepository.save(istniejacyPosilek);

        return true;
    }



}
