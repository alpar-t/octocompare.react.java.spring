package io.joggr.aaa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('ROLE_USER_MANAGER')")
public interface UserRepository extends CrudRepository<User, String> {

    @Override
    @PreAuthorize("hasRole('ROLE_USER_MANAGER') or #userName = authentication?.name")
    void delete(@Param("userName") String userName);

}
