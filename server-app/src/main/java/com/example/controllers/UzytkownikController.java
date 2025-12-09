package com.example.controllers;

import com.example.auth.jwt.JwtUtils;
import com.example.kolekcje.PrzyjacieleInfo;
import com.example.kolekcje.ZaproszenieInfo;
import com.example.kolekcje.posilki.DaniaDetail;
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


import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {
    private static final Logger log = LoggerFactory.getLogger(UzytkownikController.class);


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

        Optional<Uzytkownik> optUser = uzytkownikService.getUserByEmail(request.getEmail());
        if( optUser.isPresent() ) {
            Uzytkownik user = optUser.get();
            if( user.isBlocked() ) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
            }
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


////    ----------------------  ZAPRASZANIE DO ZNAJOMYCH    ----------------------------------------------------

    @PostMapping("/invitation/new")
    public ResponseEntity<?> sendInvitation(@RequestBody String email, Authentication authentication) {
        log.info("sendInvitation");
        if (authentication == null || email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik > optUsr = uzytkownikService.getUserByEmail(userEmail);

        if ( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        // TODO: spr czy nie są znajomyni

        log.info("{} {}", optUsr.get().getId(), email.replace("\"", ""));
        boolean spr = uzytkownikService.sendInvitation(optUsr.get().getId(), email.replace("\"", ""));
        log.info("spr: {}", spr);
        return spr
                ? ResponseEntity.ok(Map.of("message", "Wysłano zaproszenie do użytkownika"))
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Nie można utworzyć takiego zaproszenia");
    }

    @GetMapping("/invitation/all")
    public ResponseEntity<?> getAllInvitationUser(Authentication authentication) {
        log.info("getAllInvitationUser");
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik > optUsr = uzytkownikService.getUserByEmail(userEmail);
        if ( optUsr.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        // TODO: spr czy nie są znajomyni

        log.info("{}",userEmail);
        List<ZaproszenieInfo> ret = uzytkownikService.getAllZaproszenies(userEmail);
        log.info("spr: {}", ret);
        return ret != null
                ? ResponseEntity.ok().body(ret)
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Brak zaproszeń");
    }

    @PutMapping("/invitation/accept")
    public ResponseEntity<?> akceptInvitation(@RequestBody ZaproszenieInfo zaproszenie, Authentication authentication) {
        log.info("akceptInvitation");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();

        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if ( optUsr.isPresent() ) {
            boolean ret = uzytkownikService.acceptInvitation(optUsr.get(), zaproszenie);
            if( ret ) {
                ResponseEntity.ok(Map.of("message", "Zaproszenie zaakceptowane"));
            }
            else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Błąd akceptowania zaproszenia");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @PutMapping("/invitation/del")
    public ResponseEntity<?> cancelInvitation(@RequestBody ZaproszenieInfo zaproszenie, Authentication authentication) {
        log.info("cancelInvitation");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> OptUser = uzytkownikService.getUserByEmail(userEmail);
        if ( OptUser.isPresent() ) {
            boolean ret = uzytkownikService.cancelInviotationUser(zaproszenie);
            return ret ? ResponseEntity.ok(Map.of("message", "Zaproszenie odrzucone"))
                    : ResponseEntity.status(HttpStatus.CONFLICT).body("Brak autoryzacji");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }



////     -----------------------  PRZYJACIELE -------------------------------------------------


    @GetMapping("/friends")
    public ResponseEntity<?> getUserFrends(Authentication authentication) {
        log.info("cancelInvitation");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> OptUser = uzytkownikService.getUserByEmail(userEmail);
        if ( OptUser.isPresent() ) {
            List<PrzyjacieleInfo> przyjacieleInfos = new ArrayList<>();
            Uzytkownik uzytkownik = OptUser.get();

            uzytkownik.getPrzyjaciele().forEach(przyjaciel -> {
                Optional<Uzytkownik> przyjacielObj = uzytkownikService.getUserById(przyjaciel.getId());
                if( przyjacielObj.isPresent() ) {
                    Uzytkownik p = przyjacielObj.get();
                    przyjacieleInfos.add(new PrzyjacieleInfo(p.getId(), p.getImie(), p.getNazwisko(), p.getEmail(), przyjaciel.isCzyDozwolony()));
                }
            });

            return ResponseEntity.ok().body(przyjacieleInfos);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @DeleteMapping("/friends")
    public ResponseEntity<?> deleteUserFrend(@RequestBody PrzyjacieleInfo przyjacielInfo,Authentication authentication) {
        log.info("deleteUserFrend");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> OptUser = uzytkownikService.getUserByEmail(userEmail);
        if ( OptUser.isPresent() ) {
            boolean ret = uzytkownikService.deleteFriendUser(OptUser.get(), przyjacielInfo.getId());

            return ret
            ? ResponseEntity.ok(Map.of("message", "Znajomy usuniety"))
                    : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Nie usunięto przyjaciela");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @GetMapping("/friends/accesed")
    public ResponseEntity<?> getUserFrendsICanModife(Authentication authentication) {
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> OptUser = uzytkownikService.getUserByEmail(userEmail);
        if ( OptUser.isPresent() ) {
            List<PrzyjacieleInfo> przyjacieleInfos = new ArrayList<>();
            Uzytkownik uzytkownik = OptUser.get();


            uzytkownik.getPrzyjaciele().forEach(przyjaciel -> {
                Optional<Uzytkownik> przyjacielObj = uzytkownikService.getUserById(przyjaciel.getId());
                if( przyjacielObj.isPresent() ) {
                    Uzytkownik p = przyjacielObj.get();

                    Optional<Przyjaciele> mojeDane = p.getPrzyjaciele().stream()
                            .filter(pItem -> pItem.getId() == uzytkownik.getId())
                            .findFirst();

                    if( mojeDane.isPresent() && mojeDane.get().isCzyDozwolony()) {
                        przyjacieleInfos.add(new PrzyjacieleInfo(p.getId(), p.getImie(), p.getNazwisko(), p.getEmail(), przyjaciel.isCzyDozwolony()));
                    }

                }
            });

            return ResponseEntity.ok().body(przyjacieleInfos);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }

    @PutMapping("/friends")
    public ResponseEntity<?> changeAccessUserFrend (@RequestBody PrzyjacieleInfo przyjacielInfo,Authentication authentication) {
        log.info("changeAccessUserFrend");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        Optional<Uzytkownik> OptUser = uzytkownikService.getUserByEmail(userEmail);
        if ( OptUser.isPresent() ) {
            boolean ret = uzytkownikService.changeAccessUserFrend(OptUser.get(), przyjacielInfo.getId());

            return ret
                    ? ResponseEntity.ok(Map.of("message", "Znajomy ma inny dostęp"))
                    : ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Nie udało się zmienić dostępu przyjacielowi");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
    }


    @PostMapping("/recipe")
    public ResponseEntity<?> updateUserRecipe(@RequestBody List<DaniaDetail> recipes, Authentication authentication) {
        log.info("updateUserRecipe");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUser = uzytkownikService.getUserByEmail(userEmail);
        if( optUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Uzytkownik uzytkownik = optUser.get();
        uzytkownikService.upgradeUserRecipes(uzytkownik, recipes);

        return ResponseEntity.ok("Zaktualizowano przepisy uzytkownika");
    }

    @GetMapping("/recipe")
    public ResponseEntity<?> getAllUserRecipe(Authentication authentication) {
        log.info("updateUserRecipe");
        if( authentication == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        String userEmail = authentication.getName();
        Optional<Uzytkownik> optUser = uzytkownikService.getUserByEmail(userEmail);
        if( optUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        Uzytkownik uzytkownik = optUser.get();


        return ResponseEntity.ok().body(uzytkownikService.getAllUserRecipes(uzytkownik));
    }

}
