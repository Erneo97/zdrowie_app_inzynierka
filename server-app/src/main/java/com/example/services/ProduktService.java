package com.example.services;

import com.example.kolekcje.enumy.Jednostki;
import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.enumy.PoraDnia;
import com.example.kolekcje.posilki.*;
import com.example.kolekcje.statistic.ChartPoint;
import com.example.repositories.MealRepository;
import com.example.repositories.PotwierdzProduktyRepository;
import com.example.repositories.ProduktRepository;
import com.example.requests.MealUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProduktService {
    private final ProduktRepository produktyRepository;
    private final SequenceGeneratorService sequenceGenerator;
    private final MealRepository mealRepository;
    private final PotwierdzProduktyRepository potwierdzProduktyRepository;

    public ProduktService(ProduktRepository produktyRepository,
                          SequenceGeneratorService sequenceGenerator,
                          MealRepository mealRepository,
                          PotwierdzProduktyRepository potwierdzProduktyRepository) {
        this.produktyRepository = produktyRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.mealRepository = mealRepository;
        this.potwierdzProduktyRepository = potwierdzProduktyRepository;
    }

    


    public Produkt createProducts(String producent, String nazwa, String kodkreskowy, List<Dawka> objetosc, int usrInd) {
        Produkt product;


        Optional<Produkt> Optprodukt = produktyRepository.findByNazwaAndProducent(nazwa, producent);
        if (Optprodukt.isEmpty()) {
            product = new Produkt();
            product.setId(sequenceGenerator.getNextSequence(
                    LicznikiDB.PRODUKTY.getNazwa()
            ));
            product.setProducent(producent);
            product.setNazwa(nazwa);
            product.setKodKreskowy(kodkreskowy);
            product.setObjetosc(objetosc);

            produktyRepository.save(product);

            Optional<Produkt> OptProdukt = produktyRepository.findByNazwaAndProducent(nazwa, producent);
            if (OptProdukt.isPresent()) {
                Produkt fp = OptProdukt.get();
                ProduktyDoPotwierdzenia pdp = new ProduktyDoPotwierdzenia(fp.getId());
                pdp.setIdUzytkownika(usrInd);
                potwierdzProduktyRepository.save(pdp);


            }
            return product;
        }


//         Produkt zostaje zaktualizowany

        product = Optprodukt.get();

        Map<Jednostki, Dawka> istniejące =
                product.getObjetosc().stream()
                        .collect(Collectors.toMap(
                                Dawka::getJednostki,
                                d -> d,
                                (d1, d2) -> d1
                        ));


        for (Dawka nowa : objetosc) {
            Jednostki jednostka = nowa.getJednostki();

            if (istniejące.containsKey(jednostka)) {
                Dawka stara = istniejące.get(jednostka);
                stara.setWartosc(nowa.getWartosc());
                stara.setKcal(nowa.getKcal());
                stara.setBialko(nowa.getBialko());
                stara.setTluszcze(nowa.getTluszcze());
                stara.setWeglowodany(nowa.getWeglowodany());
                stara.setBlonnik(nowa.getBlonnik());
            } else {
                product.getObjetosc().add(nowa);
            }
        }

        return produktyRepository.save(product);
    }


    public List<ProduktyDoPotwierdzenia> getAllProductById(int id) {
        return potwierdzProduktyRepository.findAllByIdUzytkownika(id);
    }

    public Optional<Produkt> findById(int id) {
        return produktyRepository.findById(id);
    }


    public List<Produkt> findByNazwa(String nazwa) {return produktyRepository.findByNazwa(nazwa);}

    /**
     *
     * @param kodKreskowy
     * @return
     */
    public Optional<Produkt> findByKodKreskowy(String kodKreskowy ) {return produktyRepository.findByKodKreskowy(kodKreskowy);}


    private List<SpozyteProdukty> getMapMealUpdateToSpozyteProdukty(MealUpdate meal) {
        return meal.getMeal().stream()
                .map(item -> {
                    SpozyteProdukty nowy = new SpozyteProdukty();
                    nowy.setWartosc(item.getObjetosc());
                    nowy.setId((int) item.getId());
                    return nowy;
                })
                .toList();
    }

    public boolean createUserMeal(int userId, MealUpdate meal) {
        LocalDate localDate = LocalDate.parse(meal.getData());
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Posilki posilekNowy = new Posilki();
        posilekNowy.setId(sequenceGenerator.getNextSequence(LicznikiDB.POSILKI.getNazwa()));
        posilekNowy.setId_uzytkownika(userId);
        posilekNowy.setData(date);
        posilekNowy.setPoradnia(meal.getPoraDnia());

        List<SpozyteProdukty> noweProdukty = getMapMealUpdateToSpozyteProdukty(meal);
        posilekNowy.setProdukty(noweProdukty);
        mealRepository.save(posilekNowy);

        return true;
    }

    public boolean updateUserMeal(int userId, MealUpdate meal) {
        LocalDate localDate = LocalDate.parse(meal.getData());
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Optional<Posilki> optpos = mealRepository
                .findByIdUzytkownikaAndDataAndPoradnia(
                        userId,
                        date,
                        meal.getPoraDnia()
                );

        if (optpos.isEmpty()) {
            // nie ma takiego posiłku — nie można modyfikować
            return false;
        }

        Posilki istniejacyPosilek = optpos.get();

       List<SpozyteProdukty> noweProdukty = getMapMealUpdateToSpozyteProdukty(meal);
        istniejacyPosilek.setProdukty(noweProdukty);
        mealRepository.save(istniejacyPosilek);

        return true;
    }

    public AllMealsInDay getAllUserMealsInDay(String dateStr, int userId) {
        LocalDate localDate = LocalDate.parse(dateStr);
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        AllMealsInDay userMeal = new AllMealsInDay();


        // --- ŚNIADANIE ---
        Optional<Posilki> sniadanieOpt =
                mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                        userId, date, PoraDnia.SNIADANIE);

        if (sniadanieOpt.isPresent()) {
            List<MealInfo> sniadanie =
                    mapSpozyteProduktyToMealInfoList(sniadanieOpt.get().getProdukty());
            userMeal.setSniadanie(sniadanie);
        } else {
            userMeal.setSniadanie(Collections.emptyList());
        }

        // --- LUNCH ---
        Optional<Posilki> lunchOpt =
                mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                        userId, date, PoraDnia.LUNCH);

        if (lunchOpt.isPresent()) {
            List<MealInfo> lunch =
                    mapSpozyteProduktyToMealInfoList(lunchOpt.get().getProdukty());
            userMeal.setLunch(lunch);
        } else {
            userMeal.setLunch(Collections.emptyList());
        }

        // --- OBIAD ---
        Optional<Posilki> obiadOpt =
                mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                        userId, date, PoraDnia.OBIAD);

        if (obiadOpt.isPresent()) {
            List<MealInfo> obiad =
                    mapSpozyteProduktyToMealInfoList(obiadOpt.get().getProdukty());
            userMeal.setObiad(obiad);
        } else {
            userMeal.setObiad(Collections.emptyList());
        }

        // --- KOLACJA ---
        Optional<Posilki> kolacjaOpt =
                mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                        userId, date, PoraDnia.KOLACJA);

        if (kolacjaOpt.isPresent()) {
            List<MealInfo> kolacja =
                    mapSpozyteProduktyToMealInfoList(kolacjaOpt.get().getProdukty());
            userMeal.setKolacja(kolacja);
        } else {
            userMeal.setKolacja(Collections.emptyList());
        }

        // --- INNE ---
        Optional<Posilki> inneOpt =
                mealRepository.findByIdUzytkownikaAndDataAndPoradnia(
                        userId, date, PoraDnia.PRZEKASKA);

        if (inneOpt.isPresent()) {
            List<MealInfo> inne =
                    mapSpozyteProduktyToMealInfoList(inneOpt.get().getProdukty());
            userMeal.setInne(inne);
        } else {
            userMeal.setInne(Collections.emptyList());
        }



        return userMeal;
    }

    public List<AllMealsInDay> getAllUserMealsInDays(Date begin, Date end, int userId) {
        List<AllMealsInDay> ret = new ArrayList<>();


        return ret;
    }


    private List<MealInfo> mapSpozyteProduktyToMealInfoList(List<SpozyteProdukty> produkty) {
        return produkty.stream()
                .map(p -> {
                    Produkt produkt = produktyRepository.findById(p.getId())
                            .orElseThrow(() -> new RuntimeException(
                                    "Produkt o id=" + p.getId() + " nie istnieje"));

                    MealInfo item = new MealInfo();
                    item.setId((long)p.getId());
                    item.setObjetosc(p.getWartosc());
                    item.setNazwa(produkt.getNazwa());
                    item.setProducent(produkt.getProducent());
                    item.setKodKreskowy(produkt.getKodKreskowy());

                    return item;
                })
                .toList();
    }


    public List<Produkt> getProductList() {
        return potwierdzProduktyRepository.findAll()
                .stream()
                .filter(it -> it.getIdProduct() > -1)
                .map(it -> {
                            return produktyRepository.findById(it.getIdProduct()).get();
                        }
                )
                .toList();
    }


}
