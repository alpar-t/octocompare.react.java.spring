package io.joggr.domain;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class JogEntryTest {

    public static final String SOME_ID = "19004640-07bf-4472-8608-95bbea51c49d";
    public static final String SOME_INVALID_ID = "foobar";

    @Test
    public void getId() throws Exception {
        assertEquals(SOME_ID, (new JogEntry(SOME_ID, 1, 1, new Date(), null)).getId());
        assertEquals(36, (new JogEntry(1, 1, new Date())).getId().length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructWithInvalidID() {
        new JogEntry(SOME_INVALID_ID, 1, 1, new Date(), null);
    }

}