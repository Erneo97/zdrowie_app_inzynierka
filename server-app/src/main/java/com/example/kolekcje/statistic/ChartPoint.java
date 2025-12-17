package com.example.kolekcje.statistic;

/**
 * Klasa do zwraqcania danych do wykresów w frontend.
 *  np kcal, data na dany okres czasu
 */
public class ChartPoint {
    String x;
    Double y;

    public ChartPoint(String x, Double y) {
        this.x = x;
        this.y = y;
    }
    public String getX() {return x;}
    public Double getY() {return y;}

    public void setX(String x) {this.x = x;}
    public void setY(Double y) {this.y = y;}
}
