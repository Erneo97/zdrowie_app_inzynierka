package com.example.repositories;

import com.example.kolekcje.uzytkownik.*;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.Optional;

//@Repository
// collectionResourceRel nazwa w json gdzie będzie zwrócone
// path - url
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UzytkownikRepository extends MongoRepository<Uzytkownik, Integer> {
    Optional<Uzytkownik> findByEmail(String email);
    Optional<Uzytkownik> findById(int id);


    Optional<PomiarWagiiProjection> findProjectedById(int id);

    Optional<PrzyjacieleProjection> findPrzyjacieById(int idUser);

}