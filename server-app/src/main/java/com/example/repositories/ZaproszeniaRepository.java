package com.example.repositories;


import com.example.kolekcje.Zaproszenie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "zaproszenie", path = "zaproszenie")
public interface ZaproszeniaRepository extends MongoRepository<Zaproszenie, String> {
    Optional<Zaproszenie> findByIdZapraszanegoAndIdZapraszajacego(int idZapraszanego, int idZapraszajacego);
    List<Zaproszenie> findByIdZapraszanego(int idZapraszanego);
    void deleteById(int id);
    Optional<Zaproszenie> findById(int id);
}
