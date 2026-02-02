package com.easybook;

import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Foutih Osama 20054809
 */
class UtenteTest {

    @Test
    void testCreazioneUtente() {
        Utente utente = new Utente("CF123", "Luca", "Verdi", StatoUtente.ATTIVO, 0);

        assertEquals("CF123", utente.getCf());
        assertEquals("ATTIVO", utente.getStato());
        assertEquals(0, utente.getNumPrestitiAttivi());
    }

    @Test
    void testCambioStato() {
        Utente utente = new Utente("CF123", "Luca", "Verdi", StatoUtente.ATTIVO, 0);

        utente.setStato(StatoUtente.SOSPESO);

        assertEquals("SOSPESO", utente.getStato());
    }
}