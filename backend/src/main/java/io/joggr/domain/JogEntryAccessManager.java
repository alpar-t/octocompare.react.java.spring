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
            JogEntry entry = jogEntries.findById(jogId);
            if (entry == null) {
                logger.debug("Granting access to non existent record");
                return true;
            } else {
                if (entry.getUserName().equals(userName)) {
                    logger.debug("Access granted to {} for {}", jogId, userName);
                    return true;
                } else {
                    logger.warn("Denying access to jogId '{}' for user '{}' (the entry is owned by {})",
                            jogId, userName, entry.getUserName()
                    );
                    return false;
                }

            }
        }
    }
}
