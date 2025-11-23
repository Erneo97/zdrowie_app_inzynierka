package com.example.requests;

import com.example.kolekcje.enumy.PoraDnia;
import com.example.kolekcje.posilki.MealInfo;

import java.util.List;

public class MealUpdate {
    List<MealInfo> meal;
    String data;
    PoraDnia poraDnia;

    public MealUpdate(String data, PoraDnia poraDnia, List<MealInfo> meal) {
        this.data = data;
        this.poraDnia = poraDnia;
        this.meal = meal;
    }

    public MealUpdate() {}

    public List<MealInfo> getMeal() {return meal;}
    public String getData() {return data;}
    public PoraDnia getPoraDnia() { return poraDnia; }
    public void setMeal(List<MealInfo> meal) {this.meal = meal;}
    public void setData(String data) {this.data = data;}
    public void setPoraDnia(PoraDnia poraDnia) {this.poraDnia = poraDnia; }
}
