package com.example.services;

import com.example.kolekcje.enumy.GOAL;
import com.example.kolekcje.enumy.Makro;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.posilki.AllMealsInDay;
import com.example.kolekcje.posilki.Dawka;
import com.example.kolekcje.posilki.MealInfo;
import com.example.kolekcje.statistic.ChartPoint;
import com.example.kolekcje.statistic.StatisticParameters;
import com.example.kolekcje.uzytkownik.PommiarWagii;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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

    public static double[] linearRegression(List<Double> x, List<Double> y) {
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
            if( point.getY() > 0 )
                calories.add(point);
        }

        return calories;
    }

    private ChartPoint getUSerCaloriesByDate(int usrId , Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = formatter.format(date);
        ChartPoint chartPoint = new ChartPoint(dateStr, 0.0);
        double sum = 0.0;


        AllMealsInDay dane = produktService.getAllUserMealsInDay(dateStr, usrId);

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



    /**
     *  zwraca wartosc min, max dla daego użytkownika w zależnosci od celu w aktualnym planu treningowego.
     *  min - minimalna wartosć do spożycia danego dnia
     *  max - maxymalna wartość do spożycia danego dnia
     *
     *  Kolejność listy białko, węglowodany, tłuszcze, błonnik
     *  
     * @param user
     * @return
     */
    public List<StatisticParameters> getMakroStats(Uzytkownik user) {
        List<StatisticParameters> stats = new ArrayList<>();
        log.info("getMakroStats ");

        Optional<PlanTreningowy> optPT = treningService.getById(user.getAktualnyPlan());
        GOAL cel;
        if( optPT.isPresent() ) {
            PlanTreningowy plan = optPT.get();
            cel = plan.getCel();

        }
        else {
            cel = GOAL.CONST;
        }

        for (Makro makro : Makro.values()) {
            double[] minMax = getMinMaxGram(cel, user.getZapotrzebowanieKcal(), makro);

            StatisticParameters stat = new StatisticParameters();
            stat.setMin(minMax[0]);
            stat.setMax(minMax[1]);

            stats.add(stat);
        }
        log.info("getMakroStats {}", stats.size());

        stats.forEach( it -> log.info(it.getMin() + " " + it.getMax()));

        return stats;
    }

    private static final Map<Makro, Integer> KCAL_NA_GRAM = Map.of(
            Makro.BIALKO, 4,
            Makro.WEGLOWODANY, 4,
            Makro.TLUSZCZ, 9,
            Makro.BLONNIK, 1
    );

    private static final Map<GOAL, Map<Makro, double[]>> MAKRO_PROCENTY = Map.of(
            GOAL.CONST, Map.of(
                    Makro.BIALKO, new double[]{25, 30},
                    Makro.WEGLOWODANY, new double[]{40, 50},
                    Makro.TLUSZCZ, new double[]{25, 30},
                    Makro.BLONNIK, new double[]{0.5, 1}
            ),
            GOAL.MUSCLE, Map.of(
                    Makro.BIALKO, new double[]{25, 30},
                    Makro.WEGLOWODANY, new double[]{40, 50},
                    Makro.TLUSZCZ, new double[]{25, 30},
                    Makro.BLONNIK, new double[]{0.5, 1}
            ),
            GOAL.REDUCE, Map.of(
                    Makro.BIALKO, new double[]{30, 40},
                    Makro.WEGLOWODANY, new double[]{30, 40},
                    Makro.TLUSZCZ, new double[]{20, 30},
                    Makro.BLONNIK, new double[]{0.5, 1}
            )
    );

    private double[] getMinMaxGram(GOAL cel, int kcal, Makro makro) {
        double[] procent = MAKRO_PROCENTY.get(cel).get(makro);
        int kcalNaGram = KCAL_NA_GRAM.get(makro);

        double minGram = kcal * procent[0] / 100 / kcalNaGram;
        double maxGram = kcal * procent[1] / 100 / kcalNaGram;

        return new double[]{minGram, maxGram};
    }


}
