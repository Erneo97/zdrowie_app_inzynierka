package com.example.controllers;


import com.example.kolekcje.posilki.AllMealsInDay;
import com.example.kolekcje.posilki.Produkt;
import com.example.kolekcje.uzytkownik.Przyjaciele;
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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public ResponseEntity<?> createProduct(@RequestBody Produkt nowyProdukt, Authentication authentication) {
        log.info("createProduct  " + nowyProdukt.getNazwa());


        if( nowyProdukt.getObjetosc().isEmpty() ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Brak przypisanych wartości odrzywczych");
        }
        Optional<Uzytkownik> usrOpt = uzytkownikService.getUserByEmail(authentication.getName());
        if( usrOpt.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Produkt created = produktService.createProducts(
                nowyProdukt.getProducent(),
                nowyProdukt.getNazwa(),
                nowyProdukt.getKodKreskowy(),
                nowyProdukt.getObjetosc(),
                usrOpt.get().getId()
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



    @GetMapping("/posilek/another/all")
    public ResponseEntity<?> getMealAnotherUser( @RequestParam String date, @RequestParam String userEmail, Authentication authentication ) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String usrNname =  authentication.getName();
        Optional<Uzytkownik> optUsrSearch = uzytkownikService.getUserByEmail(userEmail);
        Optional<Uzytkownik> optUsrRes = uzytkownikService.getUserByEmail(usrNname);
        if( optUsrSearch.isEmpty() ||  optUsrRes.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        Uzytkownik usrSearch = optUsrSearch.get();
        Uzytkownik usrResp = optUsrRes.get();

        List<Przyjaciele> przyjacieles = usrSearch.getPrzyjaciele();
        AtomicBoolean isAccess = new AtomicBoolean(false);
        przyjacieles.forEach(przyjacie -> {
            if( przyjacie.getId() == usrResp.getId() && przyjacie.isCzyDozwolony() ) {
                isAccess.set(true);
            }
        });

        if( !isAccess.get() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        AllMealsInDay userMealDay = produktService.getAllUserMealsInDay(date, usrSearch.getId());

        return ResponseEntity.ok(userMealDay);
    }

    @PostMapping("/another/posilek")
    public ResponseEntity<?> createOrUpdateAnotherUserMeal(@RequestBody MealUpdate update, @RequestParam String userEmail, Authentication authentication) {
        log.info("createOrUpdateAnotherUserMeal " + "");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userReqEmail = authentication.getName();
        Optional<Uzytkownik> optReqUsr = uzytkownikService.getUserByEmail(userReqEmail);
        Optional<Uzytkownik> optEditUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optReqUsr.isPresent() && optEditUsr.isPresent() ) {
            Uzytkownik usrRe = optReqUsr.get();
            Uzytkownik usrEdit = optEditUsr.get();

            Optional<Przyjaciele> optMojeDanePrzyjazni = usrEdit.getPrzyjaciele().stream().filter(przyjacie -> przyjacie.getId() == usrRe.getId()).findFirst();
            if( optMojeDanePrzyjazni.isEmpty() || (optMojeDanePrzyjazni.isPresent() && !optMojeDanePrzyjazni.get().isCzyDozwolony())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
            }

            boolean isUpdated = produktService.updateUserMeal(usrEdit.getId(), update);
            if( !isUpdated ) {
                boolean isCreated = produktService.createUserMeal(usrEdit.getId(), update);
                return ResponseEntity.ok(Map.of("message", "Dodano nowy posiłek"));
            }


            return ResponseEntity.ok(Map.of("message", "Posiłek zaktualizowany"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @GetMapping("/posilek/all")
    public ResponseEntity<?> getMealOnDay( @RequestParam String date, Authentication authentication ) {
        log.info("getMealOnDay " + date);
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String usrNname =  authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(usrNname);
        if( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        Uzytkownik usr = optUsr.get();


        AllMealsInDay userMealDay = produktService.getAllUserMealsInDay(date, usr.getId());

        return ResponseEntity.ok(userMealDay);
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
            boolean isUpdated = produktService.updateUserMeal(usr.getId(), update);
            if( !isUpdated ) {
                boolean isCreated = produktService.createUserMeal(usr.getId(), update);
                return ResponseEntity.ok(Map.of("message", "Dodano nowy posiłek"));
            }


            return ResponseEntity.ok(Map.of("message", "Posiłek zaktualizowany"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @GetMapping("/check/produkt")
    public ResponseEntity<?> getProductsCheck(Authentication authentication) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() || !optUsr.get().getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        return ResponseEntity.ok(produktService.getProductList());
    }

    @PostMapping("/check/accept")
    public ResponseEntity<?> acceptProduct(@RequestBody int id, Authentication authentication) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() || !optUsr.get().getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        produktService.acceptProduct(id);

        return ResponseEntity.ok("Produkt potwierdzony");
    }

    @PostMapping("/check/reject")
    public ResponseEntity<?> rejectProduct(@RequestBody int id, Authentication authentication) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if( optUsr.isEmpty() || !optUsr.get().getRole().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        produktService.rejectProduct(id);

        return ResponseEntity.ok("Produkt potwierdzony");
    }


}
