package net.skhu.demo.serviceImpl;

import net.skhu.demo.domain.USER;
import net.skhu.demo.model.MyAuthenticaion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ds on 2017-10-31.
 */

@Service
public class AuthProviderImpl implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AuthorizationServiceImpl authorizationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String password = authentication.getCredentials().toString();
        return authenticate(id, password);
    }

    private Authentication authenticate(String id, String password) throws AuthenticationException {
        USER user = authorizationService.login(id, password);
        if (user == null) return null;
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
        String role = "user";
        grantedAuthorityList.add(new SimpleGrantedAuthority(role));
        return new MyAuthenticaion(id, password, grantedAuthorityList, user);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
