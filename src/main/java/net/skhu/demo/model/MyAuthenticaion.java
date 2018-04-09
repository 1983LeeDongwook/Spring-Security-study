package net.skhu.demo.model;

import lombok.Data;
import net.skhu.demo.domain.USER;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Created by ds on 2017-10-31.
 */
@Data
public class MyAuthenticaion extends UsernamePasswordAuthenticationToken {
    private  static final long serialVersionUID = 1L;

    USER user;

    public MyAuthenticaion(String id, String password, List<GrantedAuthority> grantedAuthorityList, USER user) {
        super(id, password, grantedAuthorityList);
        this.user = user;
    }
}
