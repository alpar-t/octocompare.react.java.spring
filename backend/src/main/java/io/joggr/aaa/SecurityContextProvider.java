package io.joggr.aaa;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Simple wrapper to make it possible to inject the context provider instead of the singleton
 */
@Service
public class SecurityContextProvider {

    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

}
