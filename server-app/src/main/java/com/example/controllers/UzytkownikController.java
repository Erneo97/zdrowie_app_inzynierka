package com.example.controllers;

import com.example.auth.jwt.JwtUtils;
import com.example.kolekcje.Zaproszenie;
import com.example.kolekcje.posilki.Dania;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Przyjaciele;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.LoginResponse;
import com.example.requests.LoginRequest;
import com.example.services.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {
    private static final Logger log = LoggerFactory.getLogger(UzytkownikController.class);
//    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;


    private final UzytkownikService uzytkownikService;
    private final PasswordEncoder passwordEncoder;



    public UzytkownikController(UzytkownikService uzytkownikService, PasswordEncoder passwordEncoder) {
        this.uzytkownikService = uzytkownikService;
        this.passwordEncoder = passwordEncoder;
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

        Uzytkownik created = uzytkownikService.createUser(
                user.getImie(),
                user.getNazwisko(),
                user.getEmail(),
                passwordEncoder.encode(user.getHaslo()),
                user.getWzrost(),
                user.getPlec()
        );

        return ResponseEntity.status(201).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        }
        catch (AuthenticationException e) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);


        LoginResponse loginResponse = new LoginResponse(jwtToken, userDetails.getUsername());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> userOpt = uzytkownikService.loginUser(userEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie znaleziony");
        }

        return ResponseEntity.ok(userOpt.get());
    }


    @PostMapping("/waga")
    public ResponseEntity<String> addWeitht(@RequestBody PommiarWagii request, Authentication authentication) {
        log.info("addWeitht");
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        log.info("${}  {}", userEmail, request);
        boolean ret = uzytkownikService.addUserWeights(userEmail, request);

        if( ret )
            return ResponseEntity.ok("Dodano pomiar wagi");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie znaleziony");
    }

    // UPDATE - PUT /api/uzytkownicy/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Uzytkownik> updateUser(
            @PathVariable int id,
            @RequestBody Uzytkownik updatedUser
    ) {
        // TODO: token

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
