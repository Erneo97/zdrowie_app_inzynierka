package com.example.controllers;

import com.example.kolekcje.posilki.Produkt;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.requests.MealUpdate;
import com.example.services.ProduktService;
import com.example.services.UzytkownikService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produkty")
public class ProduktController {
    private final ProduktService produktService;
    private final UzytkownikService uzytkownikService;

    public ProduktController(ProduktService produktService, UzytkownikService uzytkownikService) {
        this.produktService = produktService;
        this.uzytkownikService = uzytkownikService;
    }


    private static final Logger log = LoggerFactory.getLogger(ProduktController.class);


    @PostMapping("/produkt")
    public ResponseEntity<?> createProduct(@RequestBody Produkt nowyProdukt) {
        log.info("createProduct  " + nowyProdukt.getNazwa());


        if( nowyProdukt.getObjetosc().isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Brak przypisanych wartości odrzywczych");
        }

        Produkt created = produktService.createProducts(
                nowyProdukt.getProducent(),
                nowyProdukt.getNazwa(),
                nowyProdukt.getKodKreskowy(),
                nowyProdukt.getObjetosc()
        );

        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/produkt/{id}")
    public Optional<Produkt> findById(@PathVariable int id) {
        return produktService.findById(id);
    }

    @GetMapping("/produkty/{nazwa}")
    public List<Produkt> findByNazwa(@PathVariable String nazwa) {
        return produktService.findByNazwa(nazwa);
    }

    @GetMapping("/produkt/kod/{kodKreskowy}")
    public Optional<Produkt> findByKodKreskowy(@PathVariable String kodKreskowy) {
        return produktService.findByKodKreskowy(kodKreskowy);
    }


    @GetMapping("/posilek")
    public Optional<MealUpdate> getMeal( @RequestBody MealUpdate update ) {
        return Optional.empty();
    }

    @PostMapping("/posilek")
    public ResponseEntity<?> updateMeal(@RequestBody MealUpdate update, Authentication authentication) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isPresent() ) {
            Uzytkownik usr = optUsr.get();




            return ResponseEntity.ok().body("Brak autoryzacji");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }


}
