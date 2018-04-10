package net.skhu.demo.serviceImpl;

import net.skhu.demo.domain.*;
import net.skhu.demo.model.UserDetails;
import net.skhu.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ds on 2018-04-10.
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<USER> user = userRepository.findById(s);
        if(!user.isPresent()) {
            logger.info("loginFail");
            throw new UsernameNotFoundException("login Fail");
        }
        return new UserDetails(user.get());
    }
}
