package com.example.kolekcje.plan_treningowy;

import com.example.kolekcje.enumy.GOAL;

public class TreningsPlanCard {
    String seasonName;
    String startDate;
    String endDate;
    int trainingCount;
    boolean isActive;
    GOAL goal;

    public TreningsPlanCard() {}

    public String getSeasonName() {return seasonName;}
    public String getStartDate() {return startDate;}
    public String getEndDate() {return endDate;}
    public int getTrainingCount() {return trainingCount;}
    public boolean isActive() {return isActive;}
    public GOAL getGoal() {return goal;}

    public void setSeasonName(String seasonName) {this.seasonName = seasonName;}
    public void setStartDate(String startDate) {this.startDate = startDate;}
    public void setEndDate(String endDate) {this.endDate = endDate;}
    public void setTrainingCount(int trainingCount) {this.trainingCount = trainingCount;}
    public void setActive(boolean isActive) {this.isActive = isActive;}
    public void setGoal(GOAL goal) {this.goal = goal;}
}
