package com.example.services;

import com.example.kolekcje.statistic.StatisticParameters;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatisticService {
    private static double[] linearRegression(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Tablice muszą mieć tę samą długość");
        }

        int n = x.length;

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
        }

        double a = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - a * sumX) / n;

        return new double[]{a, b};
    }

    /**
     * Zwraca dane statystyczne kolejno dla: wagi, tk. tłuszczowe, tk. mięśniowej, nawodnienia
     * @param weights
     * @return
     */
    public List<StatisticParameters> getStatisticsForWeight(List<PommiarWagii> weights) {


    }

}
