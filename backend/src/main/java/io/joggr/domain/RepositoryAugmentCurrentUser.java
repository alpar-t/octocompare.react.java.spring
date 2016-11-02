package io.joggr.domain;

import io.joggr.aaa.Roles;
import io.joggr.aaa.SecurityContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RepositoryEventHandler(JogEntry.class)
public class RepositoryAugmentCurrentUser {

    private final Logger logger = LoggerFactory.getLogger(RepositoryAugmentCurrentUser.class);

    private final SecurityContextProvider securityContextProvider;

    @Autowired
    public RepositoryAugmentCurrentUser(SecurityContextProvider securityContextProvider) {
        this.securityContextProvider = securityContextProvider;
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    public void applyUserInformationUsingSecurityContext(JogEntry entry) {
        logger.debug("May augment jogEntry '{}' with current user", entry.getId());
        Authentication authentication = securityContextProvider.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> currentAuthorities = authentication.getAuthorities();
        String currentUser = authentication.getName();
        if (currentAuthorities.contains(Roles.ROLE_CONTENT_MANAGER)) {
            logger.info("Current user '{}' has authority '{}' ({}). Will not override user.",
                    currentUser, Roles.ROLE_CONTENT_MANAGER, currentAuthorities
            );
        } else {
            logger.info("Enforcing username {} instead of {}", currentUser, entry.getUserName());
            entry.setUserName(currentUser);
        }
    }
}
