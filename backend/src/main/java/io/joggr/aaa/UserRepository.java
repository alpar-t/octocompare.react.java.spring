package io.joggr.aaa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAnyRole('ROLE_USER_MANAGER', 'ROLE_INTERNAL')")
public interface UserRepository extends CrudRepository<User, String>, UserSignUpExtension {

    @Override
    @PreAuthorize("hasRole('ROLE_USER_MANAGER') or #userName == authentication?.name")
    void delete(@Param("userName") String userName);

    @Override
    @PreAuthorize("hasAnyRole('ROLE_USER_MANAGER', 'ROLE_INTERNAL') or #userName == authentication?.name")
    User findOne(@Param("userName") String userName);

    @Override
    @PreAuthorize("isAnonymous()")
    User signUp(String username, String password) throws UserAlreadyExistsException;
}
