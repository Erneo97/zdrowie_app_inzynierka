package com.example.controllers;

import com.example.kolekcje.posilki.Produkt;
import com.example.services.ProduktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/search")
public class SerachControler {
    private ProduktService produktService;
    private static final Logger log = LoggerFactory.getLogger(UzytkownikController.class);
    private final MongoTemplate mongoTemplate;

    public SerachControler(ProduktService produktService, MongoTemplate mongoTemplate) {
        this.produktService = produktService;
        aktualizujListeProduktow();
        this.mongoTemplate = mongoTemplate;
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
    public List<String> suggest(@RequestParam String nazwa) {
        log.info("suggest: " + nazwa);

        if (nazwa == null || nazwa.isBlank()) {
            return List.of();
        }


        Query query = new Query();
        query.addCriteria(
                Criteria.where("nazwa")
                        .regex("^" + Pattern.quote(nazwa), "i") 
        );
        query.limit(10);
        query.fields().include("nazwa");

        List<Produkt> produkty = mongoTemplate.find(query, Produkt.class, "Produkty");

        List<String> nazwy = produkty.stream()
                .map(Produkt::getNazwa)
                .toList();

        log.info(nazwy.toString());
        return nazwy;
    }
//    @GetMapping("/produkts/{nazwa}")
//    public List<Produkt> getAllProduct(@PathVariable String nazwa) {
//        log.info("getAllProduct: {}", nazwa);
//        List<String> nazwy = getAllMatchesProduktNames(nazwa).stream().distinct().collect(Collectors.toList());
//        List<Produkt> produkty = new ArrayList<>();
//        for( String nazwaSzukanegoProduktu: nazwy) {
//            produkty.addAll(produktService.findByNazwa(nazwaSzukanegoProduktu));
//        }
//
//        return produkty;
//    }

}
