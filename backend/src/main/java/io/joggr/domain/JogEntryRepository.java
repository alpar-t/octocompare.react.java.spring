package io.joggr.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or hasRole('ROLE_USER')")
public interface JogEntryRepository extends CrudRepository<JogEntry, String> {

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or #jogEntry.userName == authentication.name")
    JogEntry save(@Param("jogEntry") JogEntry entity);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    <S extends JogEntry> Iterable<S> save(Iterable<S> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or @jogEntryAccessManager.canAccess(#jogID , authentication.name)")
    JogEntry findOne(@Param("jogID")String id);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or @jogEntryAccessManager.canAccess(#jogID , authentication.name)")
    boolean exists(@Param("jogID") String id);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    Iterable<JogEntry> findAll();

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    Iterable<JogEntry> findAll(@Param("jogIDs") Iterable<String> ids);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    long count();

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or @jogEntryAccessManager.canAccess(#jogID , authentication.name)")
    void delete(@Param("jogID") String id);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or @jogEntryAccessManager.canAccess(#jogEntry.id , authentication.name)")
    void delete(@Param("jogEntry") JogEntry entity);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    void delete(Iterable<? extends JogEntry> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    void deleteAll();

    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or #userName == authentication?.name")
    List<JogEntry> findByUserName(@Param("userName") String userName);

    @PreAuthorize("hasRole('ROLE_INTERNAL')")
    @RestResource(exported = false)
    JogEntry findByIdAndUserName(String id, String userName);

}
