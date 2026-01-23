package com.easybook;

import com.easybook.controller.CatalogoController;
import com.easybook.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per il controller del catalogo libri (UC1).
 */
class CatalogoControllerTest {

    private CatalogoController controller;

    @BeforeEach
    void setUp() {
        controller = new CatalogoController();
    }

    @Test
    void testAggiungiLibro_Success() {
        Libro libro = new Libro("TEST123", "Libro Test", "Autore Test", 3, 3);

        assertDoesNotThrow(() -> controller.aggiungiLibro(libro));

        Libro trovato = controller.cercaLibro("TEST123");
        assertNotNull(trovato);
        assertEquals("Libro Test", trovato.getTitolo());

        // Cleanup
        controller.rimuoviLibro("TEST123");
    }

    @Test
    void testAggiungiLibro_ISBNDuplicato() {
        Libro libro1 = new Libro("DUP123", "Primo", "Autore", 1, 1);
        Libro libro2 = new Libro("DUP123", "Secondo", "Altro", 1, 1);

        controller.aggiungiLibro(libro1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.aggiungiLibro(libro2);
        });

        assertTrue(exception.getMessage().contains("già presente"));

        // Cleanup
        controller.rimuoviLibro("DUP123");
    }

    @Test
    void testModificaLibro_Success() {
        Libro libro = new Libro("MOD123", "Originale", "Autore", 2, 2);
        controller.aggiungiLibro(libro);

        Libro modificato = new Libro("MOD123", "Modificato", "Nuovo Autore", 3, 3);
        controller.modificaLibro(modificato);

        Libro verificato = controller.cercaLibro("MOD123");
        assertEquals("Modificato", verificato.getTitolo());
        assertEquals("Nuovo Autore", verificato.getAutore());

        // Cleanup
        controller.rimuoviLibro("MOD123");
    }

    @Test
    void testRimuoviLibro_ConPrestitiAttivi() {
        Libro libro = new Libro("DEL123", "Da Eliminare", "Autore", 5, 3);
        controller.aggiungiLibro(libro);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.rimuoviLibro("DEL123");
        });

        assertTrue(exception.getMessage().contains("copie in prestito"));

        // Cleanup (forza eliminazione)
        Libro libero = new Libro("DEL123", "Da Eliminare", "Autore", 3, 3);
        controller.modificaLibro(libero);
        controller.rimuoviLibro("DEL123");
    }
}
