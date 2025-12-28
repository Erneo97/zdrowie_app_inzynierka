package com.example.repositories;

import com.example.kolekcje.uzytkownik.UserStats;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserStatsRepository extends MongoRepository<UserStats,String > {
    Optional<UserStats> findById(int id);

}
