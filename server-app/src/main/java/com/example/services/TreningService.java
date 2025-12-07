package com.example.services;

import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.plan_treningowy.Cwiczenie;
import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import com.example.repositories.CwiczeniaRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import org.springframework.stereotype.Component;

@Component
public class TreningService {

    private final CwiczeniaRepository cwiczeniaRepository;
    private final PotwierdzProduktyRepository potwierdzProduktyRepository;
    private final SequenceGeneratorService sequenceGenerator;

    public TreningService(CwiczeniaRepository cwiczeniaRepository, PotwierdzProduktyRepository potwierdzProduktyRepository, SequenceGeneratorService sequenceGenerator) {
        this.cwiczeniaRepository = cwiczeniaRepository;
        this.potwierdzProduktyRepository = potwierdzProduktyRepository;
        this.sequenceGenerator = sequenceGenerator;
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
}
