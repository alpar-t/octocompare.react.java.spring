package io.joggr;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface JogEntryRepository extends CrudRepository<JogEntry, String> {

    List<JogEntry> findByDateBetween(Date from, Date to);

    List<JogEntry> findById(String id);

}
