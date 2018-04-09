package net.skhu.demo.serviceImpl;

import net.skhu.demo.domain.USER;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ds on 2018-04-09.
 */
public class UserDetailsImpl extends USER {

    public UserDetailsImpl(USER user) {
        super(user.getName(), user.getPw(), authorities(user));
    }

    private static Collection<? extends GrantedAuthority> authorities(USER user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }


}
