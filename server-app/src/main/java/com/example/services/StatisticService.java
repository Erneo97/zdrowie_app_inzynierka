package com.example.services;

import com.example.kolekcje.statistic.StatisticParameters;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StatisticService {

    /**
     * Zwraca dane statystyczne kolejno dla: wagi, tk. tłuszczowe, tk. mięśniowej, nawodnienia
     * @param weights
     * @return
     */
    public List<StatisticParameters> getStatisticsForWeight(List<PommiarWagii> weights) {
        List<StatisticParameters> statistics = new ArrayList<>();


        statistics.add(
                getStatisticParameters(
                        weights.stream().map(PommiarWagii::getWartosc).filter(value -> value > 0).toList()
                )
        );

        statistics.add(
                getStatisticParameters(
                        weights.stream().map(PommiarWagii::getTluszcz).filter(value -> value > 0).toList()
                )
        );

        statistics.add(
                getStatisticParameters(
                        weights.stream().map(PommiarWagii::getMiesnie).filter(value -> value > 0).toList()
                )
        );

        statistics.add(
                getStatisticParameters(
                        weights.stream().map(PommiarWagii::getNawodnienie).filter(value -> value > 0).toList()
                )
        );

        return statistics;
    }

    private static StatisticParameters getStatisticParameters(List<Double> values) {
        StatisticParameters statistics = new StatisticParameters();
        List<Double> days = IntStream.rangeClosed(1, values.size())
                .mapToDouble(i -> (double) i)
                .boxed()  // double -> Double
                .collect(Collectors.toList());

        DoubleSummaryStatistics statsWeight = values.stream().mapToDouble(Double::doubleValue).summaryStatistics();

        statistics.setMax(statsWeight.getMax());
        statistics.setMin(statsWeight.getMin());
        statistics.setAverage(statsWeight.getAverage());

        statistics.setMedian( median(values));

        double[] wynik = linearRegression(values,  days);

        statistics.setTrendLine(wynik[0], wynik[1]);

        return statistics;
    }
    private static List<Double> datesToDays(List<Date> dates) {
        if (dates == null || dates.isEmpty()) {
            throw new IllegalArgumentException("Lista dat jest pusta");
        }

        ZoneId zone = ZoneId.systemDefault();

        LocalDate baseDate = dates.getFirst().toInstant()
                .atZone(zone)
                .toLocalDate();

        return dates.stream()
                .map(d -> {
                    LocalDate current = d.toInstant()
                            .atZone(zone)
                            .toLocalDate();
                    return (double) ChronoUnit.DAYS.between(baseDate, current);
                })
                .toList();
    }

    private static double median(List<Double> items) {
        int n = items.size();

        if (n == 0) {
            throw new IllegalArgumentException("Lista pusta");
        }

        List<Double> sorted = items.stream()
                .sorted()
                .toList();

        if (n % 2 == 1) {
            return sorted.get(n / 2);
        } else {
            return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2.0;
        }
    }

    private static double[] linearRegression(List<Double> x, List<Double> y) {
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Tablice muszą mieć tę samą długość");
        }

        int n = x.size();

        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += x.get(i);
            sumY += y.get(i);
            sumXY += x.get(i) * y.get(i);
            sumX2 += x.get(i) * x.get(i);
        }

        double a = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - a * sumX) / n;

        return new double[]{a, b};
    }

}
