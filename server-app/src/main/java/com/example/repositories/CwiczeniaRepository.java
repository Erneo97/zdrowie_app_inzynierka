package com.example.repositories;

import com.example.kolekcje.plan_treningowy.Cwiczenie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CwiczeniaRepository  extends MongoRepository<Cwiczenie, String> {

}
