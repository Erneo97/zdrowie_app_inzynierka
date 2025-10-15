package com.example.services;

import com.example.kolekcje.Counter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    public SequenceGeneratorService(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public int getNextSequence(String seqName) {
        Counter counter = mongoOperations.findAndModify(
                Query.query(where("_id").is(seqName)),
                new Update().inc("seq", 1),
                Counter.class
        );

        if (counter == null) {
            counter = new Counter();
            counter.setId(seqName);
            counter.setSeq(1);
            mongoOperations.save(counter);
            return 1;
        }

        return counter.getSeq();
    }
}
