package io.joggr.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

@PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or hasRole('ROLE_USER')")
public interface JogEntryRepository extends CrudRepository<JogEntry, String> {

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or jogEntriesRepository.findOne(#jogId).userName = authentication?.name")
    JogEntry findOne(@Param("jogID")String id);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or jogEntriesRepository.findOne(#jogId).userName = authentication?.name")
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
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or jogEntriesRepository.findOne(#jogId).userName = authentication?.name")
    void delete(@Param("jogID") String id);

    @Override
    @RestResource( exported = false)
    @PreAuthorize("denyAll")
    void delete(@Param("jogEntry") JogEntry entity);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    void delete(@Param("jogEntries") Iterable<? extends JogEntry> entities);

    @Override
    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER')")
    void deleteAll();

    @PreAuthorize("hasRole('ROLE_CONTENT_MANAGER') or userName == authentication?.name")
    List<JogEntry> findByUserName(@Param("userName") String userName);

    List<JogEntry> findByDateBetween(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Param("from")
            Date from,

            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Param("to")
            Date to
    );

}
