package com.example.controllers;

import com.example.auth.jwt.JwtUtils;
import com.example.kolekcje.Zaproszenie;
import com.example.kolekcje.posilki.Dania;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Przyjaciele;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.LoginResponse;
import com.example.requests.ChangePassword;
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


    /**
     * Dodanie do rekordu użtkownika
     * @param request - zaiwera obiekt klasy PommiarWagi
     * @param authentication - TOKEN potwierdzający tożsamość osoby wpisującej dane
     * @return
     */
    @PostMapping("/waga")
    public ResponseEntity<String> addWeitht(@RequestBody PommiarWagii request, Authentication authentication) {
        log.info("addWeitht");
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        log.info("addWeitht: {}  {}", userEmail, request);
        boolean ret = uzytkownikService.addUserWeights(userEmail, request);

        if( ret )
            return ResponseEntity.ok("Dodano pomiar wagi");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Użytkownik nie znaleziony");
    }

    // UPDATE - PUT /api/uzytkownicy/update
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestBody Uzytkownik updatedUser
            , Authentication authentication
    ) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        log.info("updateUser: {}  {} {} - {}", userEmail, updatedUser, updatedUser.getDataUrodzenia(), updatedUser.getDataUrodzenia().getClass());
//        todo: zmiana tokena na nowy email po jego zmianie

        Optional<Uzytkownik> opt = uzytkownikService.loginUser(updatedUser.getEmail());
        if( opt.isPresent() && !opt.get().getEmail().equals(userEmail) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Podany email już istnieje");
        }

        Optional<Uzytkownik> user = uzytkownikService.updateUser(userEmail, updatedUser);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword password, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }


        String userEmail = authentication.getName();
        Optional<Uzytkownik> optionalUser = uzytkownikService.getUserByEmail(userEmail);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika");
        }
        Uzytkownik user = optionalUser.get();

        if( !passwordEncoder.matches(password.getOldPassword(), user.getHaslo())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne stare hasło");
        }
        log.info("zmiana hasła na: {}  {}", userEmail, password.getNewPassword());
        String passwordNew = passwordEncoder.encode(password.getNewPassword());
        uzytkownikService.updateUserPassword(userEmail, passwordNew);
        return ResponseEntity.ok(Map.of("message", "Zmieniono hasło pomyślnie"));
    }



    @PostMapping("/{id}/treningPlan")
    public void changeTreningPlan(@PathVariable int id, @RequestBody int treningPlanID) {
        uzytkownikService.updateUserTreningPlan(id, treningPlanID);
    }

    @PostMapping("/invitation/new")
    public ResponseEntity<?> sendInvitation(@RequestBody String email, Authentication authentication) {
        log.info("sendInvitation");
        if (authentication == null || email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik > optUsr = uzytkownikService.getUserByEmail(userEmail);

        if ( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nie znaleziono użytkownika");
        }

        log.info("{} {}", optUsr.get().getId(), email.replace("\"", ""));
        boolean spr = uzytkownikService.sendInvitation(optUsr.get().getId(), email.replace("\"", ""));
        log.info("spr: {}", spr);
        return spr
                ? ResponseEntity.ok(Map.of("message", "Wysłano zaproszenie do użytkownika"))
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Nie można utworzyć takiego zaproszenia");
    }

    @PostMapping("/invitation/accept")
    public void akceptInvitation(@RequestParam int id, @RequestParam int idInvitation) {
//        TODO: dodanie porzyjaciela
        Uzytkownik uzytkownik = uzytkownikService.getUserByEmail("").get();


        Optional<Zaproszenie> zaproszenie = uzytkownikService.getZaproszenieById(idInvitation);
        uzytkownikService.deleteInvitationById(idInvitation);

        Przyjaciele nowyPrzyjaciele = new Przyjaciele();
        nowyPrzyjaciele.setId(zaproszenie.get().getidZapraszajacego());


        List<Przyjaciele> przyjaciele = uzytkownik.getPrzyjaciele();
        przyjaciele.add(nowyPrzyjaciele);

        uzytkownik.setPrzyjaciele(przyjaciele);
    }


}
