package com.example.controllers;


import com.example.kolekcje.statistic.ChartPoint;
import com.example.kolekcje.statistic.StatisticInterval;
import com.example.kolekcje.statistic.StatisticParameters;
import com.example.kolekcje.statistic.StatsResponse;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.services.StatisticService;
import com.example.services.UzytkownikService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private final UzytkownikService uzytkownikService;
    private final StatisticService statisticService;
    private static final Logger log = LoggerFactory.getLogger(StatisticController.class);

    public StatisticController(UzytkownikService uzytkownikService,
                               StatisticService statisticService) {
        this.uzytkownikService = uzytkownikService;
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


    @GetMapping("/kcal")
    public ResponseEntity<?> getUserCalories(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            Date date,
            @RequestParam int countDays,
            Authentication authentication) {

        if( authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }
        String userEmail = authentication.getName();
        
        Optional<Uzytkownik> optUsr = uzytkownikService.getUserByEmail(userEmail);
        if(optUsr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Brak autoryzacji");
        }

        List<ChartPoint> kalorie = statisticService.getUserCaloriesSdata(
                optUsr.get().getId(),
                date,
                countDays
        );

        List<Double> xList = IntStream.rangeClosed(1, kalorie.size()).asDoubleStream().boxed().collect(Collectors.toList());

        double[] val = StatisticService.linearRegression(
                xList,
                kalorie.stream().mapToDouble(ChartPoint::getY).boxed().toList()
        );

        StatisticParameters sp = new StatisticParameters();
        sp.setTrendLine(val[0], val[1]);

        StatsResponse<ChartPoint> ret = new StatsResponse<>(kalorie, sp );

        return ResponseEntity.ok(ret);
    }
}
