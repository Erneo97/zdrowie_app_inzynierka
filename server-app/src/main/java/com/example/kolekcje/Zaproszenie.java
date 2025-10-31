package com.example.kolekcje;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "zaproszenie")
public class Zaproszenie {
    @Id
    int id;
    int id_zapraszajacego, id_zapraszanego;

    public void setId(int id) { this.id = id; }
    public void setId_zapraszajacego(int id_zapraszajacego) {this.id_zapraszajacego = id_zapraszajacego;}
    public void setId_zapraszanego(int id_zapraszanego) {this.id_zapraszanego = id_zapraszanego;}

    public int getId() {return id; }
    public int getId_zapraszajacego() {return id_zapraszajacego;}
    public int getId_zapraszanego() {return id_zapraszanego;}


}
