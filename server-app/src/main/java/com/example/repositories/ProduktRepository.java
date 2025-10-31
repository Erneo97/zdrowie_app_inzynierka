package com.example.repositories;


import com.example.kolekcje.posilki.Produkt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProduktRepository extends MongoRepository<Produkt, String> {

    Optional<Produkt> findById(long id);
    Optional<Produkt> findByNazwa(String name);
    Optional<Produkt> findByKodKreskowy(String kodKreskowy);

    @Query("SELECT p.nazwa FROM Produkt p")
    List<String> findAllNames();

    List<Produkt> findByProducent(String producent);


}
