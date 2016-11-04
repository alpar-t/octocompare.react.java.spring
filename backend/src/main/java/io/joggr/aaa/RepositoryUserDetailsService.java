package io.joggr.aaa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RepositoryUserDetailsService implements UserDetailsService{

    private final Logger logger = LoggerFactory.getLogger(RepositoryUserDetailsService.class);
    private final UserRepository users;

    @Autowired
    public RepositoryUserDetailsService(UserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try (AsInternalUser __ = new AsInternalUser()) {
            User one = users.findOne(username);
            if (one == null) {
                logger.info("No such user: {}", username);
                throw new UsernameNotFoundException("No such user");
            }
            logger.info("Authenticated: {}", one);
            return one;
        }
    }

}
