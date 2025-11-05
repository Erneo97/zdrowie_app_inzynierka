package com.example.auth;

import com.example.kolekcje.uzytkownik.Uzytkownik;
import com.example.repositories.UzytkownikRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements UserDetailsService {

    private final UzytkownikRepository userRepository;

    public MongoUserDetailsService(UzytkownikRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.kolekcje.uzytkownik.Uzytkownik user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znalezion:  " + username));


        return User.builder()
                .username(user.getEmail())
                .password(user.getHaslo())
                .build();
    }
}
