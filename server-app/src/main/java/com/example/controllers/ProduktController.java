package com.example.controllers;

import com.example.kolekcje.posilki.Produkt;
import com.example.services.ProduktService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/spozywcze")
public class ProduktController {
    private final ProduktService produktService;

    public ProduktController(ProduktService produktService) {
        this.produktService = produktService;
    }

    @PostMapping("/produkt")
    public ResponseEntity<?> createProduct(@RequestBody Produkt nowyProdukt) {

        if( nowyProdukt.getKodKreskowy() != null ) {
            Optional<Produkt> produkt = produktService.findByKodKreskowy(nowyProdukt.getKodKreskowy());

            if (produkt.isPresent() && produkt.get().getKodKreskowy() != null ) {
                return ResponseEntity.status(401).body("Podany kod kreskowy jest przypisany do istniejącego produktu");
            }
        }


        Produkt created = produktService.createProducts(
                nowyProdukt.getProducent(),
                nowyProdukt.getNazwa(),
                nowyProdukt.getKcal(),
                nowyProdukt.getBialko(),
                nowyProdukt.getWeglowodany(),
                nowyProdukt.getTluszcze(),
                nowyProdukt.getBlonnik(),
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


}
