package com.example.repositories;

import com.example.kolekcje.plan_treningowy.Cwiczenie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CwiczeniaRepository  extends MongoRepository<Cwiczenie, String> {
    Optional<Cwiczenie> findCwiczenieById(int id);

    List<Cwiczenie> getById(int id);
    void deleteCwiczenieById(int id);
}
