package com.example.services;

import com.example.controllers.ProduktNazwa;
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

    public Produkt createProducts(String producent, String nazwa, String kodkreskowy, List<Dawka> objetosc) {
        Produkt product = new Produkt();

        int id = sequenceGenerator.getNextSequence(LicznikiDB.PRODUKTY.getNazwa());

        product.setId(id);
        product.setProducent(producent);
        product.setNazwa(nazwa);
        product.setKodKreskowy(kodkreskowy);
        product.setObjetosc(objetosc);

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

//    FUNKCJE WYKORZYSTANE PRZEZ WYSZUKIWARKE

    public List<Produkt> findAllByProducent(String producent) {
        return produktyRepository.findByProducent(producent);
    }

    public List<String> findAllNames() {
        List<ProduktNazwa> nazwy = produktyRepository.findAllBy();
        return nazwy.stream().map(ProduktNazwa::getNazwa).toList();
    }



}
