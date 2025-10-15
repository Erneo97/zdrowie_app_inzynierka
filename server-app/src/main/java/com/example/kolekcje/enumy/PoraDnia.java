package com.example.kolekcje.enumy;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "PoraDnia")
public enum PoraDnia {
    SNIADANIE("Śniadanie"),
    LUNCH("Lunch"),
    OBIAD("Obiad"),
    KOLACJA("Kolacja"),
    PRZEKASKA("Przekąska"),;

    String poraDnia;

    private PoraDnia(String poraDnia) {
        poraDnia = poraDnia;
    }
}
