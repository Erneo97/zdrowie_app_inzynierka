package com.example.kolekcje.statistic;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StatisticInterval {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date data;
    int countDays;


    public StatisticInterval(Date data, int countDays) {
        this.data = data;
        this.countDays = countDays;
    }

    public Date getData() {return data;}
    public int getCountDays() {return countDays;}

    public void setData(Date data) {this.data = data;}
    public void setCountDays(int countDays) {this.countDays = countDays;}
}
