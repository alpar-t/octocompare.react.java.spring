package io.joggr;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    USER_ROLE,
    USER_MANAGER_ROLE,
    ADMIN_ROLE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
