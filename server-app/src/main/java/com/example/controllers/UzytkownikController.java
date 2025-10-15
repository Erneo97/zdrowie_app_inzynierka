package com.example.controllers;

import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.services.UzytkownikService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/uzytkownicy")
public class UzytkownikController {

    private final UzytkownikService uzytkownikService;

    public UzytkownikController(UzytkownikService uzytkownikService) {
        this.uzytkownikService = uzytkownikService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Uzytkownik> createUser(@RequestBody Uzytkownik user) {
        // TODO: Tworzenie tokena
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


    // READ ONE - GET /api/uzytkownicy/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Uzytkownik> getUserById(@PathVariable int id) { // @RequestHeader("Authorization") String token
        Optional<Uzytkownik> user = uzytkownikService.getUserById(id);

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
}
