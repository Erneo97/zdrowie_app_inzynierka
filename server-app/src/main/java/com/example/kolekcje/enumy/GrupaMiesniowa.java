package com.example.kolekcje.enumy;


public enum GrupaMiesniowa {

    // --- GŁÓWNE GRUPY ---
    NOGI("Nogi", null),
    PLECY("Plecy", null),
    BICEPS("Biceps", null),
    TRICEPS("Triceps", null),
    BARKI("Barki", null),
    KLATKA_PIERSIOWA("Klatka piersiowa", null),
    BRZUCH("Brzuch", null),
    PRZEDRAMIE("Przedramię", null),
    LDYKI("Łydki", null),
    KAPTURY("Kaptury", null),
    CARDIO("Cardio", null),

    // --- NOGI - PODGRUPY ---
    NOGI_CZWOROGLOWE("Nogi - czworogłowe uda", NOGI),
    NOGI_DWUGLOWE("Nogi - dwugłowe uda", NOGI),
    NOGI_POSLADKI("Nogi - pośladki", NOGI),
    NOGI_PRZYWODZICIELE("Nogi - przywodziciele", NOGI),
    NOGI_ODWODZICIELE("Nogi - odwodziciele", NOGI),

    // --- PLECY - PODGRUPY ---
    PLECY_NAJSZERSZY("Plecy - najszerszy grzbietu", PLECY),
    PLECY_PROSTOWNIKI("Plecy - prostowniki grzbietu", PLECY),
    PLECY_OBLE("Plecy - mięśnie obłe", PLECY),
    PLECY_RÓWNOLEGLOBOCZNE("Plecy - równoległoboczne", PLECY),
    PLECY_TRAPEZ_SRODKOWY_DOLNY("Plecy - czworoboczny środkowy/dolny", PLECY),

    // --- BICEPS - PODGRUPY ---
    BICEPS_GLOWA_DLUGA("Biceps - głowa długa", BICEPS),
    BICEPS_GLOWA_KROTKA("Biceps - głowa krótka", BICEPS),
    BICEPS_RAMIENNY("Biceps - mięsień ramienny", BICEPS),

    // --- TRICEPS - PODGRUPY ---
    TRICEPS_GLOWA_DLUGA("Triceps - głowa długa", TRICEPS),
    TRICEPS_GLOWA_BOCZNA("Triceps - głowa boczna", TRICEPS),
    TRICEPS_GLOWA_PRZYSRODKOWA("Triceps - głowa przyśrodkowa", TRICEPS),

    // --- BARKI - PODGRUPY ---
    BARKI_PRZEDNI("Barki - akton przedni", BARKI),
    BARKI_SRODKOWY("Barki - akton środkowy", BARKI),
    BARKI_TYLNY("Barki - akton tylny", BARKI),

    // --- KLATKA PIERSIOWA - PODGRUPY ---
    KLATKA_GORNA("Klatka piersiowa - górna", KLATKA_PIERSIOWA),
    KLATKA_SRODKOWA("Klatka piersiowa - środkowa", KLATKA_PIERSIOWA),
    KLATKA_DOLNA("Klatka piersiowa - dolna", KLATKA_PIERSIOWA),

    // --- BRZUCH - PODGRUPY ---
    BRZUCH_PROSTY("Brzuch - prosty brzucha", BRZUCH),
    BRZUCH_SKOSNE("Brzuch - skośne brzucha", BRZUCH),
    BRZUCH_POPRZECZNY("Brzuch - poprzeczny brzucha", BRZUCH),

    // --- PRZEDRAMIĘ - PODGRUPY ---
    PRZEDRAMIE_ZGINACZE("Przedramię - zginacze", PRZEDRAMIE),
    PRZEDRAMIE_PROSTOWNIKI("Przedramię - prostowniki", PRZEDRAMIE),
    PRZEDRAMIE_ROTATORY("Przedramię - rotatory", PRZEDRAMIE),

    // --- ŁYDKI - PODGRUPY ---
    LDYKI_BRZUCHATY("Łydki - mięsień brzuchaty łydki", LDYKI),
    LDYKI_PLASZCZKOWATY("Łydki - mięsień płaszczkowaty", LDYKI);


    private final String nazwa;
    private final GrupaMiesniowa parent;

    GrupaMiesniowa(String nazwa, GrupaMiesniowa parent) {
        this.nazwa = nazwa;
        this.parent = parent;
    }

    public boolean isMain() {
        return parent == null;
    }

    public GrupaMiesniowa getMainGroup() {
        return parent == null ? this : parent;
    }
}
