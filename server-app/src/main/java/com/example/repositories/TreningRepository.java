package com.example.repositories;

import com.example.kolekcje.trening.Trening;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TreningRepository extends MongoRepository<Trening, String> {
        List<Trening> findAllByIdUser(int idUser);
        Optional<Trening> findByIdTrening(int idTrening);
}
