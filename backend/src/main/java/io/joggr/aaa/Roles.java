package io.joggr.aaa;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {

    USER_ROLE,
    USER_MANAGER_ROLE;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
