package io.joggr.aaa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Service
@RestController
public class UnsecuredUserRepositoryImpl implements UserSignUpExtension {

    private final PasswordEncoder passwordEncoder;

    private final UnsecuredUserRepository users;

    @Autowired
    @Lazy
    public UnsecuredUserRepositoryImpl(PasswordEncoder passwordEncoder, UnsecuredUserRepository users) {
        this.passwordEncoder = passwordEncoder;
        this.users = users;
    }

    @Override
    @RequestMapping(path = "/users/signUp/{username}", method= RequestMethod.POST)
    public User signUp(@PathVariable("username") String username, @RequestBody String password) throws UserAlreadyExistsException {
        if ( users.exists(username) ) {
            throw new UserAlreadyExistsException();
        }
        return users.save(new User(
                username,
                passwordEncoder.encode(password),
                Collections.singleton(Roles.ROLE_USER)
        )).withObscuredPassword();
    }


}
