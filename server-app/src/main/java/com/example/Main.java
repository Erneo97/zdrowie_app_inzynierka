package com.example;

import com.example.kolekcje.enumy.Plec;
import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.services.UzytkownikService;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.context.annotation.Bean;
import com.example.repositories.UzytkownikRepository;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner init(UzytkownikService creator) {
        return args -> {
            Uzytkownik nowyUser = creator.createUser(
                    "Ewa", "Kowalski", "ewa.kowalski@example.com", "tajnehaslo", 180, Plec.MEZCZYZNA
            );
            System.out.println("Utworzono użytkownika: " + nowyUser.getId());
        };
    }

    @Bean
    CommandLineRunner showAllUsers(UzytkownikRepository repository) {
        return args -> {
            System.out.println("Lista wszystkich użytkowników w bazie:");
            repository.findAll().forEach(user ->
                    System.out.println(user.getId() + " | " + user.getImie() + " " + user.getNazwisko() + " | " + user.getEmail())
            );
        };
    }

}