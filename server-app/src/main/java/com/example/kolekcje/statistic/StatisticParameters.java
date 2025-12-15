package com.example.kolekcje.statistic;

public class StatisticParameters {
    double min, max;
    double average, median;

    double a, b;

    public double getAverage() {
        return average;
    }
    public double getMedian() {return this.median; }
    public double getMin() {return this.min; }

    public double getMax() {return this.max; }
    public double[] getTrendLine( ) {
        return new double[] { a, b };
    }

    public void setMin(double min) {this.min = min; }
    public void setMax(double max) {this.max = max; }
    public void setAverage(double average) {this.average = average; }
    public void setMedian(double median) {this.median = median; }
    public void setTrendLine(double a, double b) {
        this.a = a;
        this.b = b;
    }
}

