package net.skhu.demo.model;

import net.skhu.demo.domain.USER;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Created by ds on 2018-04-10.
 */

public class UserDetails extends User {

    public UserDetails(USER user) {
        super(user.getId(), user.getPw(), AuthorityUtils.createAuthorityList("user"));
    }
}
