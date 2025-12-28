package com.example.kolekcje.uzytkownik;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "StatystykiUzytkownik")
public class UserStats {
    @Id
    int id;
    int productReject = 0, exerciesReject;

    public int getId() {return id;}
    public void setId(int id) {this.id=id;}

    public int getProductReject() {return productReject;}
    public int getExerciesReject() {return exerciesReject;}

    public void setExerciesReject(int exerciesReject) { this.exerciesReject = exerciesReject; }
    public void setProductReject(int productReject) {this.productReject = productReject;}

    public void incrementExerciesReject() {exerciesReject++;}
    public void incrementProductReject() {productReject++;}
}
