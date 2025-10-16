package com.example.services;


import com.example.kolekcje.enumy.LicznikiDB;
import com.example.kolekcje.uzytkownik.Tokens;
import com.example.repositories.TokensRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class TokensService {
    private TokensRepository tokensRepository;
    private SequenceGeneratorService sequenceGenerator;

    public TokensService(TokensRepository tokensRepository, SequenceGeneratorService sequenceGenerator) {
        this.tokensRepository = tokensRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    public void createToken(int idPytajacego, int idDocelowego, String newTokenValue) {
        Tokens token = new Tokens(sequenceGenerator.getNextSequence(LicznikiDB.TOKENS.getNazwa()),
                idPytajacego,
                idDocelowego,
                newTokenValue);
        tokensRepository.save(token);
    }

    public void deleteById(int id) {
        tokensRepository.deleteById(id);
    }

    public void deleteTokenByUserAadFirendIds(int userId, int friendId) {
        Optional<Tokens> tokensOptional = tokensRepository.findById_pytajacegoAndId_docelowego(userId, friendId);
        tokensOptional.ifPresent(tokens -> deleteById(tokens.getId()));
    }

    public boolean isUserAuthorisedToChanges(int userId, int friendId, String token) {
        Optional<Tokens> tokensOptional = tokensRepository.findById_pytajacegoAndId_docelowego(userId, friendId);
        if (tokensOptional.isPresent()) {
            return true; // TODO: token -> tokensOptional.get().getToken().equals(token);
        }
        return false;
    }

    public boolean updateToken(int idPytajacego, int idDocelowego, String newTokenValue) {
        Optional<Tokens> existingTokenOpt =
                tokensRepository.findById_pytajacegoAndId_docelowego(idPytajacego, idDocelowego);

        if (existingTokenOpt.isEmpty()) {
        }

        Tokens t = existingTokenOpt.get(), token = new Tokens(t.getId(), t.getId_pytajacego(), t.getId_docelowego(), newTokenValue);
        tokensRepository.save(token);

        return true;
    }
}
