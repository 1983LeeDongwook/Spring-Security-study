package net.skhu.demo.repository;

import net.skhu.demo.domain.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

/**
 * Created by ds on 2018-04-10.
 */

@Transactional
public class TokenRepositoryImpl  implements PersistentTokenRepository {

    @Autowired
    TokenRepository tokenRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        logger.info("createNewToken");
        Token newToken = new Token();
        newToken.setUsername(persistentRememberMeToken.getUsername());
        newToken.setToken(persistentRememberMeToken.getTokenValue());
        newToken.setSeries(persistentRememberMeToken.getSeries());
        newToken.setLastUsed(persistentRememberMeToken.getDate());
        logger.info(newToken.toString());
        tokenRepository.save(newToken);
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        logger.info("updateToken");
        Optional<Token> token = tokenRepository.findById(series);
        if(token.isPresent()) {
            Token updateToken = token.get();
            updateToken.setToken(tokenValue);
            updateToken.setLastUsed(lastUsed);
            updateToken.setSeries(series);
            tokenRepository.save(updateToken);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {
        logger.info("persistentToken");
        Optional<Token> token = tokenRepository.findById(series);
        if(token.isPresent()) {
            PersistentRememberMeToken persistentRememberMeToken =
                    new PersistentRememberMeToken(token.get().getUsername(), series, token.get().getToken(), token.get().getLastUsed());
            return persistentRememberMeToken;
        }else {
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        logger.info("removeToken");
        Optional<Token> token = tokenRepository.findByUsername(username);
        logger.info(token.get().toString());
        if(token.isPresent()) tokenRepository.delete(token.get());
    }
}