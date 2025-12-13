package com.example.services;

import com.example.controllers.UzytkownikController;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.plan_treningowy.TreningsPlanCard;
import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.CwiczeniaRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import com.example.repositories.TreningPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        nowyPlan.setId_uzytkownia(uzytkownik.getId());
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
    private static final Logger log = LoggerFactory.getLogger(TreningService.class);
    public List<TreningsPlanCard> getAllTreningPlans(Uzytkownik uzytkownik) {

        Query query = new Query();
        query.addCriteria(Criteria.where("id_uzytkownia").in(uzytkownik.getId()));

        List<PlanTreningowy> planyUzytkownika = mongoTemplate.find(query, PlanTreningowy.class, "PlanTreningowy");

        List<TreningsPlanCard> treningPlanCards = new ArrayList<>();

        planyUzytkownika.forEach(item -> {
            TreningsPlanCard nowy = new TreningsPlanCard();
            nowy.setId(item.getId());
            nowy.setSeasonName(item.getNazwa());
            nowy.setIsActive( (uzytkownik.getAktualnyPlan() == item.getId()));
            log.info("tp  " + uzytkownik.getAktualnyPlan() + " " + item.getId()+ "   " + (uzytkownik.getAktualnyPlan() == item.getId()));
            nowy.setGoal(item.getCel());
            nowy.setStartDate(item.getDataUtworzenia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());

            nowy.setEndDate(item.getDataUtworzenia().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString()); // TODO: zmienić na datę ostatniego treningu
            nowy.setTrainingCount(0); // TODO; dodać zliczanie treningów danego planu

            treningPlanCards.add(nowy);
        });

        return treningPlanCards;
    }

}
