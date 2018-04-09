package net.skhu.demo.service;

import net.skhu.demo.domain.USER;

/**
 * Created by ds on 2017-10-27.
 */

public interface AuthorizationService {
    USER login(final String id, final String pw);
    USER getCurrentUser();
    void setCurrentUser(final USER user);
}
