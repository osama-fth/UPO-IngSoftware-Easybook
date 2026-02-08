package com.easybook.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Riccardo Negrini 20054675
 */

public class SanzioneTest {
    @Test
    void testCreazioneSanzione() {
        Sanzione sanzione = new Sanzione(1, 10.5);

        assertEquals(1, sanzione.getIdprestito());
        assertEquals(10.5, sanzione.getImporto());
        assertFalse(sanzione.isPagata()); // Corretto: isPagata() restituisce boolean
    }

    @Test
    void testCreazioneSanzioneCompleta() {
        Sanzione sanzione = new Sanzione(1, 2, 15.0, true);

        assertEquals(1, sanzione.getId());
        assertEquals(2, sanzione.getIdprestito());
        assertEquals(15.0, sanzione.getImporto());
        assertTrue(sanzione.isPagata());
    }
}
