package com.example.controllers;


import com.example.kolekcje.statistic.StatisticInterval;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.services.TreningService;
import com.example.services.UzytkownikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private final UzytkownikService uzytkownikService;
    private final TreningService treningService;

    public StatisticController(UzytkownikService uzytkownikService, TreningService treningService) {
        this.uzytkownikService = uzytkownikService;
        this.treningService = treningService;
    }

    @GetMapping("/weight")
    public ResponseEntity<?> getUserWeight(@RequestBody StatisticInterval interval, Authentication authentication) {
        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();

        List<PommiarWagii> wagi = uzytkownikService.getUserWeightsByDate(userEmail, interval.getData(), interval.getCountDays());



        return ResponseEntity.ok(wagi);
    }
}
