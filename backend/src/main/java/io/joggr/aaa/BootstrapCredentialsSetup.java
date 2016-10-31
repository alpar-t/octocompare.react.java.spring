package io.joggr.aaa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class BootstrapCredentialsSetup {

    public static final String DEFAULT_ADMIN_NAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "password";

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public BootstrapCredentialsSetup(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void doSetup() {
        if (! users.exists(DEFAULT_ADMIN_NAME)) {
            users.save(new User(DEFAULT_ADMIN_NAME, passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD), Arrays.asList(Roles.values())));
        }
    }
}
