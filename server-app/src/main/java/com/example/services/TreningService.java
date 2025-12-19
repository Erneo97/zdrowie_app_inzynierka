package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.plan_treningowy.CwiczeniaPlanuTreningowego;
import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.plan_treningowy.TreningsPlanCard;
import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import com.example.kolekcje.trening.CwiczenieWTreningu;
import com.example.kolekcje.trening.Trening;
import com.example.kolekcje.trening.TreningCard;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.CwiczeniaRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import com.example.repositories.TreningPlanRepository;
import com.example.repositories.TreningRepository;
import com.example.requests.CwiczeniaPlanuTreningowegoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TreningService {

    private final CwiczeniaRepository cwiczeniaRepository;
    private final PotwierdzProduktyRepository potwierdzProduktyRepository;
    private final TreningPlanRepository treningPlanRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final UzytkownikService uzytkownikService;
    private final TreningRepository treningRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TreningService(CwiczeniaRepository cwiczeniaRepository,
                          TreningPlanRepository treningPlanRepository,
                          PotwierdzProduktyRepository potwierdzProduktyRepository,
                          TreningRepository treningRepository,
                          SequenceGeneratorService sequenceGenerator, UzytkownikService uzytkownikService) {

        this.cwiczeniaRepository = cwiczeniaRepository;
        this.treningPlanRepository = treningPlanRepository;
        this.potwierdzProduktyRepository = potwierdzProduktyRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.uzytkownikService = uzytkownikService;
        this.treningRepository = treningRepository;
    }

    public Optional<PlanTreningowy> getById(int id) {
        return treningPlanRepository.findById(id);
    }

    public Cwiczenie createExercise(Cwiczenie cwiczenia) {
        Cwiczenie nowe = new Cwiczenie();

        nowe.setId(sequenceGenerator.getNextSequence(
                LicznikiDB.CWICZENIA.getNazwa()
        ));
        nowe.setNazwa(cwiczenia.getNazwa());
        nowe.setOpis(cwiczenia.getOpis());
        nowe.setMet(cwiczenia.getMet());
        nowe.setGrupaMiesniowas(cwiczenia.getGrupaMiesniowas());

        ProduktyDoPotwierdzenia pdp = new ProduktyDoPotwierdzenia(nowe.getId());
        potwierdzProduktyRepository.save(pdp);

        cwiczeniaRepository.save(nowe);
        return nowe;
    }

    public void createTreningPlan(Uzytkownik uzytkownik, PlanTreningowy nowyPlan, Boolean aktualnyPlan ) {
        nowyPlan.setIdUzytkownia(uzytkownik.getId());
        nowyPlan.setId(sequenceGenerator.getNextSequence(LicznikiDB.PLANY_TRENINGOWE.getNazwa()));
        String isoDate = LocalDate.now().toString();
        nowyPlan.setDataUtworzenia(Date.from(
                LocalDate.parse(isoDate)
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant())
        );

        treningPlanRepository.save(nowyPlan);
        if( aktualnyPlan ) {
            uzytkownik.setAktualnyPlan(nowyPlan.getId());
            uzytkownikService.updateUser(uzytkownik);
        }
    }

    public void updateTreningPlan(Uzytkownik uzytkownik, PlanTreningowy nowyPlan, Boolean aktualnyPlan ) {
        nowyPlan.setIdUzytkownia(uzytkownik.getId());

        Optional<PlanTreningowy > opt = treningPlanRepository.findById(nowyPlan.getId());
        if( opt.isEmpty() ) {
            return;
        }
        PlanTreningowy now = opt.get();
        nowyPlan.setDataUtworzenia(now.getDataUtworzenia());

        treningPlanRepository.save(nowyPlan);
        if( aktualnyPlan ) {
            uzytkownik.setAktualnyPlan(nowyPlan.getId());
            uzytkownikService.updateUser(uzytkownik);
        }
    }

    public List<CwiczeniaPlanuTreningowegoResponse> getExerciseTreningPlan(Uzytkownik uzytkownik, int id ) {

        Optional<PlanTreningowy> optPT = treningPlanRepository.findById(id);
        if( optPT.isEmpty() ) {
            return null;
        }
        List<CwiczeniaPlanuTreningowegoResponse> list = new ArrayList<>();
        PlanTreningowy plan = optPT.get();

        if( plan.getIdUzytkownia() == uzytkownik.getId() ) {
            List<CwiczeniaPlanuTreningowego> cw = plan.getCwiczeniaPlanuTreningowe();

            cw.forEach( cwiczenieWPlanie -> {
                    Optional<Cwiczenie> optCw = getCwiczenieById(cwiczenieWPlanie.getId());
                    if( optCw.isPresent()) {
                        Cwiczenie znalezione = optCw.get();
                        CwiczeniaPlanuTreningowegoResponse nowy = new CwiczeniaPlanuTreningowegoResponse();

                        nowy.setId(cwiczenieWPlanie.getId());

                        nowy.setGrupaMiesniowas(znalezione.getGrupaMiesniowas());
                        nowy.setNazwa(znalezione.getNazwa());

                        nowy.setSerie(cwiczenieWPlanie.getSerie());
                        list.add(nowy);
                    }
            });

            return list;
        }


        return null;
    }

    public Optional<Cwiczenie> getCwiczenieById(int id) {
        return cwiczeniaRepository.findCwiczenieById(id);
    }

    private static final Logger log = LoggerFactory.getLogger(TreningService.class);
    public List<TreningsPlanCard> getAllTreningPlans(Uzytkownik uzytkownik) {

        List<PlanTreningowy> planyUzytkownika = treningPlanRepository.findByIdUzytkownia(uzytkownik.getId());

        List<TreningsPlanCard> treningPlanCards = new ArrayList<>();

        planyUzytkownika.forEach(item -> {
            TreningsPlanCard nowy = new TreningsPlanCard();
            nowy.setId(item.getId());
            nowy.setSeasonName(item.getNazwa());
            nowy.setIsActive( (uzytkownik.getAktualnyPlan() == item.getId()));

            nowy.setGoal(item.getCel());
            nowy.setStartDate(item.getDataUtworzenia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

            List<Trening> trenings = treningRepository.findAllByIdUser(uzytkownik.getId()).stream().filter( it -> it.getIdPlanu() == item.getId()).toList();

            Optional<Trening> optLastTrening = trenings.stream().max(Comparator.comparing(Trening::getData));

            String endDate = item.getDataUtworzenia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
            if( optLastTrening.isPresent() ) {
                endDate = optLastTrening.get().getData().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
            }

            nowy.setEndDate(endDate);

            nowy.setTrainingCount(trenings.size());

            treningPlanCards.add(nowy);
        });
        log.info(treningPlanCards.toString());
        return treningPlanCards;
    }

    public Trening createTrenicBasedOnTreningPlan(PlanTreningowy plan, Uzytkownik uzytkownik) {
        Trening trening = new Trening();

        trening.setIdTrening(-1);
        trening.setIdUser(uzytkownik.getId());
        trening.setIdPlanu(plan.getId());

        LocalDate today = LocalDate.now();

        trening.setData(Date.from(
                LocalDate.parse(today.toString())
                        .atStartOfDay(ZoneOffset.UTC)
                        .toInstant())
        );


        List<CwiczenieWTreningu> wykonaneCwiczenia = plan.getCwiczeniaPlanuTreningowe()
                .stream().map(
                item -> {
            CwiczenieWTreningu nowe = new CwiczenieWTreningu();

            Optional<Cwiczenie> optCw = getCwiczenieById(item.getId());
            if( optCw.isEmpty()) {
                return null;
            }

            nowe.setId(item.getId());
            nowe.setNazwa(optCw.get().getNazwa());

            nowe.setSerie(item.getSerie());
            nowe.setCzas("00:00");

            return nowe;
        }).toList();

        trening.setCwiczenia(wykonaneCwiczenia);

        return trening;
    }

    public int updateTrening(Trening trening, Uzytkownik uzytkownik) {

        if (trening.getIdUser() != uzytkownik.getId()) {
            throw new RuntimeException("Brak uprawnień");
        }

        if (trening.getIdTrening() == -1) {
            trening.setIdTrening(
                    sequenceGenerator.getNextSequence(LicznikiDB.TRENINGI.getNazwa())
            );
            treningRepository.save(trening);
            return trening.getIdTrening();
        }



        treningRepository.save(trening);

        return trening.getIdTrening();
    }

    public List<TreningCard> getTreningCards(int userId) {
        List<TreningCard> treningCards = new ArrayList<>();

        List<Trening> trenings = treningRepository.findAllByIdUser(userId);

        trenings.forEach( trening -> {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = formatter.format(trening.getData());


            TreningCard treningCard = new TreningCard();

            treningCard.setIdTrening(trening.getIdTrening());
            treningCard.setSpaloneKalorie(trening.getSpaloneKalorie());
            treningCard.setDate(dateStr);

            String nazwa = treningPlanRepository.findById(trening.getIdPlanu()).get().getNazwa();

            treningCard.setNazwa(nazwa);
            treningCard.setIloscCwiczen(
                   trening.getCwiczenia().size()
            );


           treningCards.add(treningCard);
        });

        return treningCards.stream().sorted(Comparator.comparing(
                tc -> LocalDate.parse(tc.getDate(), formatter ), Comparator.reverseOrder()))
                .toList();
    }


}
