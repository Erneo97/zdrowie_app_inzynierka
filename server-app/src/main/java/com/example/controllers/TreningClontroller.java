package com.example.controllers;

import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.services.TreningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trening")
public class TreningClontroller {
    private final TreningService treningService;

    private static final Logger log = LoggerFactory.getLogger(TreningClontroller.class);


    public TreningClontroller(TreningService treningService) {
        this.treningService = treningService;
    }


    @PostMapping("/exercise/new")
    public ResponseEntity<?> createExercise(@RequestBody Cwiczenie noweCwiczenie) {
        log.info("createExercise " + noweCwiczenie.getNazwa());
        if( noweCwiczenie.getGrupaMiesniowas().isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Brak przypisanych wartości odrzywczych");
        }
        Cwiczenie created = treningService.createExercise(noweCwiczenie);

        return ResponseEntity.status(201).body(created);
    }
}
