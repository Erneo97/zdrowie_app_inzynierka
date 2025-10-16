package com.example.repositories;


import com.example.kolekcje.uzytkownik.Tokens;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "tokens", path = "tokens")
public interface TokensRepository extends MongoRepository<Tokens, Integer> {
    Optional<Tokens> findById_pytajacegoAndId_docelowego(int id_pytajacego, int id_docelowego);

    void deleteById(int id);
}
