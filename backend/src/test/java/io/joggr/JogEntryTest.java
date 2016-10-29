package io.joggr;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class JogEntryTest {

    public static final String SOME_ID = "someId";

    @Test
    public void getId() throws Exception {
        assertEquals(SOME_ID, (new JogEntry(SOME_ID, 1, 1, new Date())).getId());
        assertEquals(36, (new JogEntry(1, 1, new Date())).getId().length());
    }

}