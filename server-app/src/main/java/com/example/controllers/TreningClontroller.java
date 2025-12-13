package com.example.controllers;

import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.requests.CwiczeniaPlanuTreningowegoResponse;
import com.example.services.TreningService;
import com.example.services.UzytkownikService;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trening")
public class TreningClontroller {
    private final TreningService treningService;
    private final UzytkownikService uzytkownikService;

    private static final Logger log = LoggerFactory.getLogger(TreningClontroller.class);


    public TreningClontroller(TreningService treningService, UzytkownikService uzytkownikService) {
        this.treningService = treningService;
        this.uzytkownikService = uzytkownikService;
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

    @PostMapping("/treningPlan")
    public ResponseEntity<?> createTreningPlan(@RequestBody PlanTreningowy nowyPlan, @RequestParam boolean aktualny,
                                               Authentication authentication) {
        log.info("createTreningPlan " + nowyPlan.getNazwa() + " " + nowyPlan.getCwiczeniaPlanuTreningowe()  + " " + authentication.getName());
        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Uzytkownik usr = optUsr.get();

        treningService.createTreningPlan(usr, nowyPlan, aktualny);

        return ResponseEntity.status(201).body("Utworzo nowy plan treningowy");
    }

    @GetMapping("/treningPlan/{id}")
    public ResponseEntity<?> getExerciseTreningPlan(@RequestParam int id,  Authentication authentication) {
        log.info("getExerciseTreningPlan {}", id);
        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Uzytkownik usr = optUsr.get();

        List<CwiczeniaPlanuTreningowegoResponse> ret = treningService.getExerciseTreningPlan(usr, id);
        log.info("getExerciseTreningPlan" + ret.toString());
        if( ret == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brak rzadanych danych");
        }

        return ResponseEntity.status(201).body(ret);
    }


    @PostMapping("/treningPlan/update")
    public ResponseEntity<?> updateTreningPlan(@RequestBody PlanTreningowy nowyPlan, @RequestParam boolean aktualny,
                                               Authentication authentication) {
        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Uzytkownik usr = optUsr.get();

        treningService.updateTreningPlan(usr, nowyPlan, aktualny);

        return ResponseEntity.status(201).body("Utworzo nowy plan treningowy");
    }

    @GetMapping("/treningPlan")
    public ResponseEntity<?> getAllTreningPlans( Authentication authentication) {

        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        Uzytkownik usr = optUsr.get();

        return ResponseEntity.status(201).body(treningService.getAllTreningPlans(usr));
    }



}
