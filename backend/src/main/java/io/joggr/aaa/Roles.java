package io.joggr.aaa;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    ROLE_USER,
    ROLE_USER_MANAGER,
    ROLE_CONTENT_MANAGER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
