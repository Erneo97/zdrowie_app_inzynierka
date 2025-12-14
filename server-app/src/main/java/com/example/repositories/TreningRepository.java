package com.example.repositories;

import com.example.kolekcje.trening.Trening;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TreningRepository extends MongoRepository<Trening, String> {

}
