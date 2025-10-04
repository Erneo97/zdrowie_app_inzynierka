package com.example.kolekcje;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
public class Counter {
    @Id
    private String idUser;
    private int seq;

    public String getId() { return idUser; }
    public void setId(String id) { this.idUser = id; }

    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
}
