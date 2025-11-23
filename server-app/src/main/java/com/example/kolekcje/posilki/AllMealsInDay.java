package com.example.kolekcje.posilki;

import java.util.List;


/**
 * Struktura zwracana gdy użytkownik żąda wszystkich posiłków w ciagu danego dnia, inicjalizacja całego dnia
 */
public class AllMealsInDay {
    List<MealInfo> sniadanie, lunch, obiad, kolacja, inne;

    public List<MealInfo> getKolacja() {return kolacja;}
    public void setKolacja(List<MealInfo> kolacja) {this.kolacja = kolacja;}

    public List<MealInfo> getSniadanie() {return sniadanie;}
    public void setSniadanie(List<MealInfo> sniadanie) {this.sniadanie = sniadanie;}

    public List<MealInfo> getLunch() {return lunch;}
    public void setLunch(List<MealInfo> lunch) {this.lunch = lunch;}

    public List<MealInfo> getObiad() {return obiad;}
    public void setObiad(List<MealInfo> obiad) {
        this.obiad = obiad;
    }

    public void setInne(List<MealInfo> inne) {
        this.inne = inne;
    }
    public List<MealInfo> getInne() {return inne;}
}
