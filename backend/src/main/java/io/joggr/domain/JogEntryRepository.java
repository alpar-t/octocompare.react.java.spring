package io.joggr.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

@PreAuthorize("hasRole('ROLE_USER')")
public interface JogEntryRepository extends CrudRepository<JogEntry, String> {

    List<JogEntry> findByDateBetween(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Param("from")
            Date from,

            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @Param("to")
            Date to
    );

}
