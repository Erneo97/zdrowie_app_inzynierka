package com.example.controllers;


import com.example.kolekcje.statistic.StatisticInterval;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.services.StatisticService;
import com.example.services.TreningService;
import com.example.services.UzytkownikService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private final UzytkownikService uzytkownikService;
    private final TreningService treningService;
    private final StatisticService statisticService;
    private static final Logger log = LoggerFactory.getLogger(StatisticController.class);

    public StatisticController(UzytkownikService uzytkownikService, TreningService treningService, StatisticService statisticService) {
        this.uzytkownikService = uzytkownikService;
        this.treningService = treningService;
        this.statisticService = statisticService;
    }

    @PostMapping("/weight")
    public ResponseEntity<?> getUserWeight(@RequestBody StatisticInterval interval, Authentication authentication) {
       log.info("getUserWeight authentication {}", authentication);

        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        log.info("getUserWeight dane  - {} {}: {} {}", userEmail, interval, interval.getData(), interval.getCountDays());

        List<PommiarWagii> wagi = uzytkownikService.getUserWeightsByDate(userEmail, interval.getData(), interval.getCountDays());
        log.info("getUserWeight weight size {} ", wagi.size());
        if(!wagi.isEmpty() )
            return ResponseEntity.ok(statisticService.getStatisticsForWeight(wagi));


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wybranym zakresie nie ma danych");
    }
}
