package com.example.controllers;

import com.example.kolekcje.posilki.Produkt;
import com.example.services.ProduktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/serach")
public class SerachControler {
    private ProduktService produktService;
    private static final Logger log = LoggerFactory.getLogger(UzytkownikController.class);

    public SerachControler(ProduktService produktService) {
        this.produktService = produktService;
        aktualizujListeProduktow();
    }

    List<String> listaProduktow;

    private void aktualizujListeProduktow() {
        this.listaProduktow = produktService.findAllNames().stream().toList();
    }

    /**
     * Zwraca wszystkie nazwy produktów których odległość levenstein od szukanego stringa jest <= 2
     * @param nazwa
     * @return
     */
    @GetMapping("/produkty")
    public List<String> getAllMatchesProduktNames(@RequestBody String nazwa) {
        log.info("getAllProduktowName: {}", nazwa);
        return LevenshteinMatcher.matchParallel(listaProduktow, nazwa, 2);
    }

    @GetMapping("/produkts/{nazwa}")
    public List<Produkt> getAllProduct(@PathVariable String nazwa) {
        log.info("getAllProduct: {}", nazwa);
        List<String> nazwy = getAllMatchesProduktNames(nazwa).stream().distinct().collect(Collectors.toList());
        List<Produkt> produkty = new ArrayList<>();
        for( String nazwaSzukanegoProduktu: nazwy) {
            produkty.addAll(produktService.findByNazwa(nazwaSzukanegoProduktu));
        }

        return produkty;
    }

}
