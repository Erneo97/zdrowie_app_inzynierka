package com.example.repositories;

import com.example.kolekcje.plan_treningowy.PlanTreningowy;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;


@RepositoryRestResource(collectionResourceRel = "planTreningowy", path  = "planTreningowy" )
public interface TreningPlanRepository extends MongoRepository<PlanTreningowy, String> {
    Optional<PlanTreningowy> findById(int id);
}
