package io.joggr.aaa;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Allows wrapping code that needs to authenticate as internal role
 *
 * usage:
 * <code>
 *     try (AsInternalUser __ = new AsInternalUser()) {
 *         ...
 *     }
 * </code>
 */
public class AsInternalUser implements AutoCloseable {

    private final SecurityContext previousContext;

    public AsInternalUser() {
        previousContext = SecurityContextHolder.getContext();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(
                new AnonymousAuthenticationToken("INTERNAL","INTERNAL_USERNAME", AuthorityUtils.createAuthorityList("ROLE_INTERNAL"))
        );
        SecurityContextHolder.setContext(
                context
        );
    }

    @Override
    public void close() {
        if (previousContext == null) {
            SecurityContextHolder.clearContext();
        } else {
            SecurityContextHolder.setContext(previousContext);
        }
    }
}
