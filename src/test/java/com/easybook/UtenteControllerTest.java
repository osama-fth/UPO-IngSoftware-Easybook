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
        String cf = "RSSMRA85M01H501Z";
        Utente utente = new Utente(cf, "Mario", "Rossi", StatoUtente.ATTIVO, 0);

        // Pulizia preventiva
        try {
            controller.rimuoviUtente(cf);
        } catch (Exception ignored) {
        }

        assertDoesNotThrow(() -> controller.registraUtente(utente), "La registrazione dovrebbe avvenire con successo");

        Utente trovato = controller.cercaUtente(cf);
        assertNotNull(trovato, "L'utente dovrebbe essere presente nel DB");
        assertEquals("Mario", trovato.getNome());
        assertEquals("Rossi", trovato.getCognome());

        // Cleanup finale
        controller.rimuoviUtente(cf);
    }

    @Test
    void testRegistraUtente_CFDuplicato() {
        String cf = "BNCGLI90A41F205X";
        Utente utente1 = new Utente(cf, "Giulia", "Bianchi", StatoUtente.ATTIVO, 0);
        Utente utente2 = new Utente(cf, "Giulia", "Bianchi", StatoUtente.ATTIVO, 0);

        try {
            controller.rimuoviUtente(cf);
        } catch (Exception ignored) {
        }

        controller.registraUtente(utente1);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.registraUtente(utente2);
        });

        assertEquals("Utente con CF BNCGLI90A41F205X già registrato.", e.getMessage());

        // Cleanup
        controller.rimuoviUtente(cf);
    }

    @Test
    void testSospendiUtente() {
        String cf = "VRDLCU80A01H501W";
        Utente utente = new Utente(cf, "Luca", "Verdi", StatoUtente.ATTIVO, 0);

        try {
            controller.rimuoviUtente(cf);
        } catch (Exception ignored) {
        }
        controller.registraUtente(utente);

        controller.sospendiUtente(cf);

        Utente verificato = controller.cercaUtente(cf);
        assertEquals("SOSPESO", verificato.getStato(), "Lo stato dell'utente dovrebbe essere SOSPESO");

        // Cleanup
        controller.rimuoviUtente(cf);
    }

    @Test
    void testRimuoviUtente_ConPrestitiAttivi() {
        String cf = "NRIFNC95E41H501Q";
        Utente utente = new Utente(cf, "Francesca", "Neri", StatoUtente.ATTIVO, 2);

        try {
            controller.rimuoviUtente(cf);
        } catch (Exception ignored) {
        }
        controller.registraUtente(utente);

        // Tento di rimuovere: dovrebbe fallire perché ha libri in prestito
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.rimuoviUtente(cf);
        });

        assertEquals("Impossibile eliminare: l'utente ha 2 prestiti attivi.", e.getMessage());

        // Cleanup: Per eliminare, devo azzerare i prestiti (simulo restituzione)
        Utente utenteSenzaPrestiti = new Utente(cf, "Francesca", "Neri", StatoUtente.ATTIVO, 0);
        controller.modificaUtente(utenteSenzaPrestiti);

        assertDoesNotThrow(() -> controller.rimuoviUtente(cf), "Ora dovrebbe essere rimovibile");
    }
}