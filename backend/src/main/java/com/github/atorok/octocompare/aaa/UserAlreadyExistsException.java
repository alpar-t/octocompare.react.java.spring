package com.github.atorok.octocompare.aaa;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="Username already exists")
public class UserAlreadyExistsException extends Exception {
}
