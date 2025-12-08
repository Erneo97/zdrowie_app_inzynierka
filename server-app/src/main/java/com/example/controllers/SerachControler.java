package com.example.controllers;

import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.posilki.Produkt;
import com.example.services.ProduktService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        this.mongoTemplate = mongoTemplate;
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

        Criteria nameCriteria = Criteria.where("nazwa").regex(Pattern.quote(nazwa), "i");
        Criteria producerCriteria = Criteria.where("producent").regex(Pattern.quote(nazwa), "i");

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(nameCriteria, producerCriteria));
        query.limit(15);
        query.fields().include("nazwa").include("producent");

        List<Produkt> produkty = mongoTemplate.find(query, Produkt.class, "Produkty");

        List<String> nazwy = produkty.stream()
                .map(p -> p.getProducent() + " - " + p.getNazwa())
                .toList();

        log.info(nazwy.toString());
        return nazwy;
    }

    /**
     * Zwraca wszystkie nazwy ćwiczeń, któych nazwa pokrywa zawiera znaki zawarte w parm nazwa.
     * @param nazwa
     * @return
     */
    @GetMapping("/cwiczenia")
    public List<String> suggestExercise(@RequestParam String nazwa) {
        log.info("suggestExercise: " + nazwa);

        if (nazwa == null || nazwa.isBlank()) {
            return List.of();
        }

        Criteria nameCriteria = Criteria.where("nazwa").regex(Pattern.quote(nazwa), "i");

        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(nameCriteria));
        query.limit(15);
        query.fields().include("nazwa");

        List<Cwiczenie> cwiczenia = mongoTemplate.find(query, Cwiczenie.class, "Cwiczenia");

        List<String> nazwy = cwiczenia.stream()
                .map(Cwiczenie::getNazwa)
                .toList();

        log.info(nazwy.toString());
        return nazwy;
    }


    @GetMapping("/produkts/{nazwa}")
    public List<Produkt> getAllProduct(@PathVariable String nazwa) {
        log.info("getAllProduct: {}", nazwa);

        if (nazwa == null || nazwa.isBlank()) {
            return List.of();
        }

        String[] fragments = nazwa.split("-");
        List<Criteria> criteriaList = new ArrayList<>();

        for (String fragment : fragments) {
            fragment = fragment.trim();
            if (!fragment.isEmpty()) {
                Criteria nameCriteria = Criteria.where("nazwa").regex(Pattern.quote(fragment), "i");
                Criteria producerCriteria = Criteria.where("producent").regex(Pattern.quote(fragment), "i");
                criteriaList.add(new Criteria().orOperator(nameCriteria, producerCriteria));
            }
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        List<Produkt> produkty = mongoTemplate.find(query, Produkt.class, "Produkty");

        log.info("Znaleziono produkty: {}", produkty.size());
        return produkty;
    }

}
