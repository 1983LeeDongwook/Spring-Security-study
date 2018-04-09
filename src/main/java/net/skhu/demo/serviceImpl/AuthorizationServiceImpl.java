package net.skhu.demo.serviceImpl;

import net.skhu.demo.domain.USER;
import net.skhu.demo.model.MyAuthenticaion;
import net.skhu.demo.repository.UserRepository;
import net.skhu.demo.service.AuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ds on 2017-10-27.
 */

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository userRepository;

    @Override
    public USER login(final String id, final String pw) {
        Optional<USER> user = userRepository.findById(id);
        logger.info("id : " + id + " pw : " + pw);
        if(!user.isPresent()) {
            logger.info("no user");
            return null;
        }
        else {
            if(user.get().getPw().equals(pw)) return user.get();
            else {
                logger.info("not match pw");
                return null;
            }
        }
    }

    @Override
    public USER getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof MyAuthenticaion)
            return ((MyAuthenticaion) authentication).getUser();
        return null;
    }

    @Override
    public void setCurrentUser(USER user) {
        ((MyAuthenticaion)
                SecurityContextHolder.getContext().getAuthentication()).setUser(user);
    }
}
