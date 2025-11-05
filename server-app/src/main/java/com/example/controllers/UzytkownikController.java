package com.example.controllers;

import com.example.auth.JwtResponse;
import com.example.kolekcje.Zaproszenie;
import com.example.kolekcje.posilki.Dania;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Przyjaciele;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.requests.LoginRequest;
import com.example.services.UzytkownikService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {
    private static final Logger log = LoggerFactory.getLogger(UzytkownikController.class);
//    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UzytkownikService uzytkownikService;


    public UzytkownikController(UzytkownikService uzytkownikService) {
        this.uzytkownikService = uzytkownikService;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody Uzytkownik user) {
        Optional<Uzytkownik> retUser = uzytkownikService.loginUser(user.getEmail());
        if( retUser.isPresent() ) {
            return ResponseEntity.status(401).body("Podany email już istnieje");
        }

        log.info("Tworzenie użytkownika: {}", user.getEmail());
//        String hashedPassword = passwordEncoder.encode(user.getHaslo());
        Uzytkownik created = uzytkownikService.createUser(
                user.getImie(),
                user.getNazwisko(),
                user.getEmail(),
                user.getHaslo(),
                user.getWzrost(),
                user.getPlec()
        );

        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Uzytkownik> retUser = uzytkownikService.loginUser(request.getEmail() );
        log.info("Logowanie użytkownika : {}", request.getEmail());
        log.info("retUser : {}", retUser.isPresent());

        if (retUser.isPresent()) {
            Uzytkownik user = retUser.get();

//            if (passwordEncoder.matches(request.getPassword(), user.getHaslo())) {
            if (request.getPassword().equals( user.getHaslo())) {
                return ResponseEntity.ok(new JwtResponse("token", user.getId()));
         }
        }

        return ResponseEntity.status(401).body("Niepoprawny login lub hasło");
    }

    // READ ONE - GET /api/uzytkownicy/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Uzytkownik> getUserById(@PathVariable int id) { // @RequestHeader("Authorization") String token
        Optional<Uzytkownik> user = uzytkownikService.getUserById(id);
        log.info("Pobranie danych użytkownika o id : {}", id);

//        if( user.isPresent() && user.get().isTokenCorrect(token)) {
//            return ResponseEntity.status(401).build();
//        }

        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE - PUT /api/uzytkownicy/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Uzytkownik> updateUser(
            @PathVariable int id,
            @RequestBody Uzytkownik updatedUser
    ) {
        // TODO: token
//        String hashedPassword = passwordEncoder.encode(updatedUser.getHaslo());
//        updatedUser.setHaslo(hashedPassword);

        Optional<Uzytkownik> user = uzytkownikService.updateUser(id, updatedUser);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Możliwośc usunięcia użytkownika na podstawie wskazanego id
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        // TODO: token
        boolean deleted = uzytkownikService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Wszystkie pomiary wag użytkownika o danym id
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/weight")
    public List<PommiarWagii> getUserWeight(@PathVariable int id) {
        // TODO: token
        return uzytkownikService.getUsersWeights(id);
    }

    @PostMapping("/{id}/weight")
    public void setUserWeight(@PathVariable int id, @RequestBody List<PommiarWagii> weights) {
        uzytkownikService.updateUserWeights(id, weights);
    }

    @PostMapping("/{id}/password")
    public void changePassword(@PathVariable int id, @RequestBody String password) {
        uzytkownikService.updateUserPassword(id, password);
    }

    @PostMapping("/meals")
    public void addMealsUser( @RequestBody Dania danie) {

    }


    @PostMapping("/{id}/treningPlan")
    public void changeTreningPlan(@PathVariable int id, @RequestBody int treningPlanID) {
        uzytkownikService.updateUserTreningPlan(id, treningPlanID);
    }

    @PostMapping("/invitation/new")
    public ResponseEntity<?> sendInvitation(@RequestParam String email, @RequestParam int id) {
        return uzytkownikService.sendInvitation(id, email) ? ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/invitation/accept")
    public void akceptInvitation(@RequestParam int id, @RequestParam int idInvitation) {
//        TODO: dodanie porzyjaciela
        Uzytkownik uzytkownik = uzytkownikService.getUserById(id).get();


        Optional<Zaproszenie> zaproszenie = uzytkownikService.getZaproszenieById(idInvitation);
        uzytkownikService.deleteInvitationById(idInvitation);

        Przyjaciele nowyPrzyjaciele = new Przyjaciele();
        nowyPrzyjaciele.setId(zaproszenie.get().getidZapraszajacego());


        List<Przyjaciele> przyjaciele = uzytkownik.getPrzyjaciele();
        przyjaciele.add(nowyPrzyjaciele);

        uzytkownik.setPrzyjaciele(przyjaciele);
    }


}
