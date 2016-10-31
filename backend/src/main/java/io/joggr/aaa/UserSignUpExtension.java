package io.joggr.aaa;

public interface UserSignUpExtension {

    User signUp(String username, String password) throws UserAlreadyExistsException;

}
