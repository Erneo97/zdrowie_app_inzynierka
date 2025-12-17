package com.example.services;

import com.example.kolekcje.posilki.AllMealsInDay;
import com.example.kolekcje.posilki.Dawka;
import com.example.kolekcje.posilki.MealInfo;
import com.example.kolekcje.statistic.ChartPoint;
import com.example.kolekcje.statistic.StatisticParameters;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StatisticService {
    private final TreningService treningService;
    private final ProduktService produktService;

    public StatisticService(TreningService treningService, ProduktService produktService) {
        this.treningService = treningService;
        this.produktService = produktService;
    }

    private static final Logger log = LoggerFactory.getLogger(StatisticService.class);

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



    public List<ChartPoint> getUserCaloriesSdata(int usrId, Date dataKoncowa, int dayCount ) {
        List<ChartPoint> calories = new ArrayList<>();

        ZoneId zone = ZoneId.systemDefault();

        LocalDate endDate = dataKoncowa
                .toInstant()
                .atZone(zone)
                .toLocalDate();

        LocalDate startDate = endDate.minusDays(dayCount);

        for (LocalDate date = startDate; !date.isAfter(endDate);  date = date.plusDays(1) ) {

            Date currentDate = Date.from(
                    date.atStartOfDay(zone).toInstant()
            );

            ChartPoint point = getUSerCaloriesByDate(usrId, currentDate);
            calories.add(point);
        }

        return calories;
    }

    private ChartPoint getUSerCaloriesByDate(int usrId , Date date) {
        ChartPoint chartPoint = new ChartPoint(date.toString(), 0.0);
        double sum = 0.0;

        AllMealsInDay dane = produktService.getAllUserMealsInDay(date.toString(), usrId);

        for (MealInfo pos : dane.getSniadanie()) {
            sum += pos.getObjetosc().getKcal();
        }
        for (MealInfo pos : dane.getLunch()) {
            sum += pos.getObjetosc().getKcal();
        }
        for (MealInfo pos : dane.getObiad()) {
            sum += pos.getObjetosc().getKcal();
        }
        for (MealInfo pos : dane.getKolacja()) {
            sum += pos.getObjetosc().getKcal();
        }
        for (MealInfo pos : dane.getInne()) {
            sum += pos.getObjetosc().getKcal();
        }

        chartPoint.setY(sum);

        return chartPoint;
    }


    private List<ChartPoint> getUserMakrosByDate(int usrId , Date date) {
        List<ChartPoint> chartPoint = new ArrayList<>();
        double sumBialko = 0.0, sumTluszcz = 0.0, sumWeglo  = 0.0, sumBlonnik = 0.0;

        AllMealsInDay dane = produktService.getAllUserMealsInDay(date.toString(), usrId);

        for (MealInfo pos : dane.getSniadanie()) {
            Dawka dawka = pos.getObjetosc();

            sumBlonnik += dawka.getBlonnik();
            sumBialko += dawka.getBialko();
            sumWeglo += dawka.getWeglowodany();
            sumTluszcz += dawka.getTluszcze();
        }
        for (MealInfo pos : dane.getLunch()) {
            Dawka dawka = pos.getObjetosc();

            sumBlonnik += dawka.getBlonnik();
            sumBialko += dawka.getBialko();
            sumWeglo += dawka.getWeglowodany();
            sumTluszcz += dawka.getTluszcze();
        }
        for (MealInfo pos : dane.getObiad()) {
            Dawka dawka = pos.getObjetosc();

            sumBlonnik += dawka.getBlonnik();
            sumBialko += dawka.getBialko();
            sumWeglo += dawka.getWeglowodany();
            sumTluszcz += dawka.getTluszcze();
        }
        for (MealInfo pos : dane.getKolacja()) {
            Dawka dawka = pos.getObjetosc();

            sumBlonnik += dawka.getBlonnik();
            sumBialko += dawka.getBialko();
            sumWeglo += dawka.getWeglowodany();
            sumTluszcz += dawka.getTluszcze();
        }
        for (MealInfo pos : dane.getInne()) {
            Dawka dawka = pos.getObjetosc();

            sumBlonnik += dawka.getBlonnik();
            sumBialko += dawka.getBialko();
            sumWeglo += dawka.getWeglowodany();
            sumTluszcz += dawka.getTluszcze();
        }

        chartPoint.add(new ChartPoint(date.toString(), sumBialko));
        chartPoint.add(new ChartPoint(date.toString(), sumWeglo));
        chartPoint.add(new ChartPoint(date.toString(), sumTluszcz));
        chartPoint.add(new ChartPoint(date.toString(), sumBlonnik));


        return chartPoint;
    }

}
