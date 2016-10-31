package io.joggr.aaa;

import org.springframework.data.repository.CrudRepository;

public interface UnsecuredUserRepository extends CrudRepository<User, String>, UserSignUpExtension {
}
