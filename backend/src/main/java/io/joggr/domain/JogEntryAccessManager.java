package io.joggr.domain;

import io.joggr.aaa.AsInternalUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class JogEntryAccessManager {

    private final Logger logger = LoggerFactory.getLogger(JogEntryAccessManager.class);
    private final JogEntryRepository jogEntries;

    @Autowired
    public JogEntryAccessManager(JogEntryRepository jogEntries) {
        this.jogEntries = jogEntries;
    }

    public boolean canAccess(String jogId, String userName) {
        Objects.nonNull(jogId);
        Objects.nonNull(userName);
        try (AsInternalUser __ = new AsInternalUser()) {
            JogEntry entry = jogEntries.findByIdAndUserName(jogId, userName);
            if (entry == null) {
                logger.info("Denying access to jogId '{}' of user '{}' (did not find this)", jogId, userName);
                return false;
            } else {
                return true;
            }
        }
    }
}
