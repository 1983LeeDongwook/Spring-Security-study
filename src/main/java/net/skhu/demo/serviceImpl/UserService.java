package net.skhu.demo.serviceImpl;

import net.skhu.demo.domain.USER;
import net.skhu.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * Created by ds on 2018-04-09.
 */
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<USER> user = userRepository.findById(s);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(s);
        } else {
            //return new UserDetailsImpl(user.get());
            return null;
        }
    }
}
