package io.joggr;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

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
