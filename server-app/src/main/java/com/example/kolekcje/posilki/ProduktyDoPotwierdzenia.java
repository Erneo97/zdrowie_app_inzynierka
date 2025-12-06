package com.example.kolekcje.posilki;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Klasa potiwerdzajaca czy dany produkt spełnia wymagania, przez admina.
 *
 * posiada id produktu dodanego, użytkownika, kóry dodał ten produkt.
 *
 */
@Document( collection = "PotwierdzProdukty")
public class ProduktyDoPotwierdzenia {
        private long idProduct;
        private Date createdAt;


        public ProduktyDoPotwierdzenia() {}

        public ProduktyDoPotwierdzenia(long idProduct) {
            this.idProduct = idProduct;
            this.createdAt = new Date();
        }

        public long getIdProduct() {return idProduct;}
        public void setIdProduct(long idProduct) {this.idProduct = idProduct;}

        public Date getCreatedAt() {return createdAt;}
        public void setCreatedAt(Date createdAt) {this.createdAt = createdAt;}
}
