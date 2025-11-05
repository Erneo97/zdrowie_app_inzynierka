package com.example.kolekcje;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "zaproszenie")
public class Zaproszenie {
    @Id
    int id;
    int idZapraszajacego, idZapraszanego;

    public void setId(int id) { this.id = id; }
    public void setidZapraszajacego(int idZapraszajacego) {this.idZapraszajacego = idZapraszajacego;}
    public void setidZapraszanego(int idZapraszanego) {this.idZapraszanego = idZapraszanego;}

    public int getId() {return id; }
    public int getidZapraszanego() {return idZapraszanego;}
    public int getidZapraszajacego() {return idZapraszajacego;}


}
