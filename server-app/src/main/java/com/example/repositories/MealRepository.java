package com.example.repositories;

import com.example.kolekcje.posilki.Posilki;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.Optional;


@RepositoryRestResource(collectionResourceRel = "meals", path = "meals")
public interface MealRepository extends MongoRepository<Posilki, String> {
    Optional <Posilki> findById( int id);
    Optional <Posilki> findByIdUzytkownikaAndData( int id, Date data);
}
