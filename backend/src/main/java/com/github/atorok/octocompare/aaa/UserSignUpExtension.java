package com.github.atorok.octocompare.aaa;

public interface UserSignUpExtension {

    User signUp(String username, String password) throws UserAlreadyExistsException;

}
