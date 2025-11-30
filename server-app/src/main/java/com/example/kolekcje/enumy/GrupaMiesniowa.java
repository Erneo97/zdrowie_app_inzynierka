package com.example.kolekcje.enumy;


public enum GrupaMiesniowa {

    // --- GŁÓWNE GRUPY ---
    NOGI("Nogi"),
    PLECY("Plecy"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    BARKI("Barki"),
    KLATKA_PIERSIOWA("Klatka piersiowa"),
    BRZUCH("Brzuch"),
    PRZEDRAMIE("Przedramie"),
    LDYKI("Łydki"),
    KAPTURY("Kaptury"),

    // --- NOGI – PODGRUPY ---
    NOGI_CZWOROGLOWE("Czworogłowe uda"),
    NOGI_DWUGLOWE("Dwugłowe uda"),
    NOGI_POSLADKI("Pośladki"),
    NOGI_PRZYWODZICIELE("Przywodziciele"),
    NOGI_ODWODZICIELE("Odwodziciele"),

    // --- PLECY – PODGRUPY ---
    PLECY_NAJSZERSZY("Najszeroki grzbietu"),
    PLECY_PROSTOWNIKI("Prostowniki grzbietu"),
    PLECY_OBLE("Mięśnie obłe"),
    PLECY_RÓWNOLEGLOBOCZNE("Równoległoboczne"),
    PLECY_TRAPEZ_SRODKOWY_DOLNY("Czworoboczny środkowy/dolny"),

    // --- BICEPS – PODGRUPY ---
    BICEPS_GLOWA_DLUGA("Biceps – głowa długa"),
    BICEPS_GLOWA_KROTKA("Biceps – głowa krótka"),
    BICEPS_RAMIENNY("Mięsień ramienny"),

    // --- TRICEPS – PODGRUPY ---
    TRICEPS_GLOWA_DLUGA("Triceps – głowa długa"),
    TRICEPS_GLOWA_BOCZNA("Triceps – głowa boczna"),
    TRICEPS_GLOWA_PRZYSRODKOWA("Triceps – głowa przyśrodkowa"),

    // --- BARKI – PODGRUPY ---
    BARKI_PRZEDNI("Barki – akton przedni"),
    BARKI_SRODKOWY("Barki – akton środkowy"),
    BARKI_TYLNY("Barki – akton tylny"),

    // --- KLATKA PIERSIOWA – PODGRUPY ---
    KLATKA_GORNA("Klatka piersiowa – górna"),
    KLATKA_SRODKOWA("Klatka piersiowa – środkowa"),
    KLATKA_DOLNA("Klatka piersiowa – dolna"),

    // --- BRZUCH – PODGRUPY ---
    BRZUCH_PROSTY("Prosty brzucha"),
    BRZUCH_SKOSNE("Skośne brzucha"),
    BRZUCH_POPRZECZNY("Poprzeczny brzucha"),

    // --- PRZEDRAMIĘ – PODGRUPY ---
    PRZEDRAMIE_ZGINACZE("Przedramię – zginacze"),
    PRZEDRAMIE_PROSTOWNIKI("Przedramię – prostowniki"),
    PRZEDRAMIE_ROTATORY("Przedramię – rotatory"),

    // --- ŁYDKI – PODGRUPY ---
    LDYKI_BRZUCHATY("Mięsień brzuchaty łydki"),
    LDYKI_PLASZCZKOWATY("Mięsień płaszczkowaty");



    private final String grupaNazwa;

    GrupaMiesniowa(String grupaNazwa) {
        this.grupaNazwa = grupaNazwa;
    }

    public String getGrupaNazwa() {
        return grupaNazwa;
    }
}