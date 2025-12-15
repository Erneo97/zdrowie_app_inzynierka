package com.example;

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
    CommandLineRunner showAllUsers(UzytkownikRepository repository) {
        return args -> {
            System.out.println("Lista wszystkich użytkowników w bazie:");
            repository.findAll().forEach(user ->
                    System.out.println(user.getId() + " | " + user.getImie() + " " + user.getNazwisko() + " | " + user.getEmail())
            );
        };
    }

}