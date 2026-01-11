package com.example.repositories;

import com.example.kolekcje.posilki.ProduktyDoPotwierdzenia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "potwierdzProdukty", path = "potwierdzProdukty")
public interface PotwierdzProduktyRepository extends MongoRepository<ProduktyDoPotwierdzenia,String > {
    Optional<ProduktyDoPotwierdzenia> findByIdProduct(int idProduct);

    Optional<ProduktyDoPotwierdzenia> findByIdExercise(int idExercise);
    void deleteByIdProduct(int idProduct);
    void deleteByIdExercise(int idExercise);

    List<ProduktyDoPotwierdzenia> findAllByIdUzytkownika(int idUzytkownika);
}
