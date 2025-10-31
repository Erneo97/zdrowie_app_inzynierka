package com.example.repositories;


import com.example.controllers.ProduktNazwa;
import com.example.kolekcje.posilki.Produkt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProduktRepository extends MongoRepository<Produkt, String> {

    Optional<Produkt> findById(long id);
    List<Produkt> findByNazwa(String name);
    Optional<Produkt> findByKodKreskowy(String kodKreskowy);

    List<ProduktNazwa> findAllBy();

    List<Produkt> findByProducent(String producent);


}
