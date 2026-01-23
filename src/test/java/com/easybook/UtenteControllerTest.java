package com.easybook;

import com.easybook.controller.UtenteController;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il controller degli utenti (UC2).
 */
class UtenteControllerTest {

    private UtenteController controller;

    @BeforeEach
    void setUp() {
        controller = new UtenteController();
    }

    @Test
    void testRegistraUtente_Success() {
        Utente utente = new Utente("TSTCF01H01L219A", "Nome", "Cognome", StatoUtente.ATTIVO, 0);

        assertDoesNotThrow(() -> controller.registraUtente(utente));

        Utente trovato = controller.cercaUtente("TSTCF01H01L219A");
        assertNotNull(trovato);
        assertEquals("Nome", trovato.getNome());

        // Cleanup
        controller.rimuoviUtente("TSTCF01H01L219A");
    }

    @Test
    void testRegistraUtente_CFDuplicato() {
        Utente utente1 = new Utente("DUPCF01H01L219A", "Primo", "Utente", StatoUtente.ATTIVO, 0);
        Utente utente2 = new Utente("DUPCF01H01L219A", "Secondo", "Altro", StatoUtente.ATTIVO, 0);

        controller.registraUtente(utente1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.registraUtente(utente2);
        });

        assertTrue(exception.getMessage().contains("già registrato"));

        // Cleanup
        controller.rimuoviUtente("DUPCF01H01L219A");
    }

    @Test
    void testSospendiUtente() {
        Utente utente = new Utente("SUSPCF01H01L219A", "Da Sospendere", "Cognome", StatoUtente.ATTIVO, 0);
        controller.registraUtente(utente);

        controller.sospendiUtente("SUSPCF01H01L219A");

        Utente verificato = controller.cercaUtente("SUSPCF01H01L219A");
        assertEquals("SOSPESO", verificato.getStato());

        // Cleanup
        controller.rimuoviUtente("SUSPCF01H01L219A");
    }

    @Test
    void testRimuoviUtente_ConPrestitiAttivi() {
        Utente utente = new Utente("DELCF01H01L219A", "Con Prestiti", "Cognome", StatoUtente.ATTIVO, 2);
        controller.registraUtente(utente);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.rimuoviUtente("DELCF01H01L219A");
        });

        assertTrue(exception.getMessage().contains("prestiti attivi"));

        // Cleanup (forza eliminazione)
        Utente libero = new Utente("DELCF01H01L219A", "Con Prestiti", "Cognome", StatoUtente.ATTIVO, 0);
        controller.modificaUtente(libero);
        controller.rimuoviUtente("DELCF01H01L219A");
    }
}
