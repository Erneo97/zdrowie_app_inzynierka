package com.example.services;

import com.example.controllers.UzytkownikController;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.plan_treningowy.CwiczeniaPlanuTreningowego;
import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.plan_treningowy.TreningsPlanCard;
import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.CwiczeniaRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import com.example.repositories.TreningPlanRepository;
import com.example.requests.CwiczeniaPlanuTreningowegoResponse;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Component
public class TreningService {

    private final CwiczeniaRepository cwiczeniaRepository;
    private final PotwierdzProduktyRepository potwierdzProduktyRepository;
    private final TreningPlanRepository treningPlanRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final UzytkownikService uzytkownikService;
    private final MongoTemplate mongoTemplate;

    public TreningService(CwiczeniaRepository cwiczeniaRepository,
                          TreningPlanRepository treningPlanRepository,
                          PotwierdzProduktyRepository potwierdzProduktyRepository,
                          MongoTemplate mongoTemplate,
                          SequenceGeneratorService sequenceGenerator, UzytkownikService uzytkownikService) {
        this.cwiczeniaRepository = cwiczeniaRepository;
        this.treningPlanRepository = treningPlanRepository;
        this.potwierdzProduktyRepository = potwierdzProduktyRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.uzytkownikService = uzytkownikService;
        this.mongoTemplate = mongoTemplate;
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

            nowy.setEndDate(item.getDataUtworzenia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()); // TODO: zmienić na datę ostatniego treningu
            nowy.setTrainingCount(0); // TODO; dodać zliczanie treningów danego planu

            treningPlanCards.add(nowy);
        });
        log.info(treningPlanCards.toString());
        return treningPlanCards;
    }

}
