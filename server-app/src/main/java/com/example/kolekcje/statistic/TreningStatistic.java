package com.example.kolekcje.statistic;

import com.example.kolekcje.enumy.GrupaMiesniowa;
import com.example.kolekcje.trening.CwiczenieWTreningu;

import java.util.List;
import java.util.Map;


public class TreningStatistic {
    String nazwa, dateCurrent, datePrevious;
    List<CwiczenieWTreningu> trening;
    float spaloneKalorie;
    Map<GrupaMiesniowa, Float> current, previous;

    public void setNazwa(String nazwa) {this.nazwa = nazwa; }
    public void setDateCurrent(String dateCurrent) {this.dateCurrent = dateCurrent;}
    public void setDatePrevious(String datePrevious) {this.datePrevious = datePrevious;}
    public void setTrening(List<CwiczenieWTreningu> trening) {this.trening = trening;}
    public void setSpaloneKalorie(float spaloneKalorie) {this.spaloneKalorie = spaloneKalorie;}

    public void setCurrent(Map<GrupaMiesniowa, Float> current) {this.current = current; }
    public void setPrevious(Map<GrupaMiesniowa, Float> previous) { this.previous = previous; }

    public String getNazwa() {return nazwa;}
    public String getDateCurrent() {return dateCurrent;}
    public String getDatePrevious() {return datePrevious;}
    public List<CwiczenieWTreningu> getTrening() {return trening;}
    public float getSpaloneKalorie() {return spaloneKalorie;}

    public Map<GrupaMiesniowa, Float> getCurrent() {return current;}
    public Map<GrupaMiesniowa, Float> getPrevious() {return previous;}
}


