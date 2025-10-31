package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.posilki.Dawka;
import com.example.kolekcje.posilki.Produkt;
import com.example.repositories.ProduktRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProduktService {
    private ProduktRepository produktyRepository;
    private final SequenceGeneratorService sequenceGenerator;

    public ProduktService(ProduktRepository produktyRepository, SequenceGeneratorService sequenceGenerator) {
        this.produktyRepository = produktyRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    public Produkt createProducts(String producent, String nazwa, float kcal, float bialko, float weglowodany, float tluszczem, float blonnik, String kodkreskowy, Dawka objetosc) {
        Produkt product = new Produkt();

        int id = sequenceGenerator.getNextSequence(LicznikiDB.PRODUKTY.getNazwa());

        product.setId(id);
        product.setProducent(producent);
        product.setNazwa(nazwa);
        product.setKcal(kcal);
        product.setBialko(bialko);
        product.setWeglowodany(weglowodany);
        product.setTluszcze(tluszczem);
        product.setBlonnik(blonnik);
        product.setKodKreskowy(kodkreskowy);
        product.setObjetosc(objetosc);

        return produktyRepository.save(product);
    }

    public Optional<Produkt> findById(int id) {
        return produktyRepository.findById(id);
    }


    public Optional<Produkt> findByNazwa(String nazwa) {return produktyRepository.findByNazwa(nazwa);}

    /**
     *
     * @param kodKreskowy
     * @return
     */
    public Optional<Produkt> findByKodKreskowy(String kodKreskowy ) {return produktyRepository.findByKodKreskowy(kodKreskowy);}

//    FUNKCJE WYKORZYSTANE PRZEZ WYSZUKIWARKE

    public List<Produkt> findAllByProducent(String producent) {
        return produktyRepository.findByProducent(producent);
    }

    public List<String> findAllNames() {return produktyRepository.findAllNames();}



}
