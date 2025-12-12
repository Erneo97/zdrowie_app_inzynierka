package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.CwiczeniaRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import com.example.repositories.TreningPlanRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class TreningService {

    private final CwiczeniaRepository cwiczeniaRepository;
    private final PotwierdzProduktyRepository potwierdzProduktyRepository;
    private final TreningPlanRepository treningPlanRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final UzytkownikService uzytkownikService;

    public TreningService(CwiczeniaRepository cwiczeniaRepository,
                          TreningPlanRepository treningPlanRepository,
                          PotwierdzProduktyRepository potwierdzProduktyRepository,
                          SequenceGeneratorService sequenceGenerator, UzytkownikService uzytkownikService) {
        this.cwiczeniaRepository = cwiczeniaRepository;
        this.treningPlanRepository = treningPlanRepository;
        this.potwierdzProduktyRepository = potwierdzProduktyRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.uzytkownikService = uzytkownikService;
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

}
