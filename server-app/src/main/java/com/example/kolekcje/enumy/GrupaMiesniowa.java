package com.example.kolekcje.enumy;

public enum GrupaMiesniowa {
    NOGI("Nogi"),
    PLECY("Plecy"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    BARKI("Barki"),
    KLATKA_PIERSIOWA("Klatka piersiowa"),
    BRZUCH("Brzuch"),
    PRZEDRAMIE("Przedramie"),
    LDYKI("Łdyki"),
    KAPTURY("Kaptury");

    private String grupaNazwa;

    private GrupaMiesniowa(String grupaNazwa) {this.grupaNazwa = grupaNazwa;}
}
