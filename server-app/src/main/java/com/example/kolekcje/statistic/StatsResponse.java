package com.example.kolekcje.statistic;

import java.util.List;

public class StatsResponse<T> {
    List<T> data;
    StatisticParameters stats;

    public StatsResponse(List<T> data, StatisticParameters stats) {
        this.data = data;
        this.stats = stats;
    }

    public List<T> getData() {return data;}
    public StatisticParameters getStats() {return stats;}

    public void setData(List<T> data) {this.data = data;}
    public void setStats(StatisticParameters stats) {this.stats = stats;}
}
