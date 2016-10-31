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
    private final UnsecuredUserRepository users;

    @Autowired
    public RepositoryUserDetailsService(UnsecuredUserRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Looking up {}", username);
        User one = users.findOne(username);
        logger.info("Found: {}", one);
        if (one == null) {
            throw new UsernameNotFoundException("No such user");
        }
        return one;
    }

}
