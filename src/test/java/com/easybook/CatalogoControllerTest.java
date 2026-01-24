package com.easybook;

import com.easybook.controller.CatalogoController;
import com.easybook.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per il controller del catalogo libri (UC1).
 */
class CatalogoControllerTest {

    private CatalogoController controller;

    @BeforeEach
    void setUp() {
        controller = new CatalogoController();
    }

    @Test
    void testAggiungiLibro_Success() {
        String isbn = "978-8804669863";
        Libro libro = new Libro(isbn, "Lo scudo di Talos", "Valerio Massimo Manfredi", 3, 3);

        // Assicuriamoci che non esista prima del test
        try {
            controller.rimuoviLibro(isbn);
        } catch (Exception ignored) {
        }

        assertDoesNotThrow(() -> controller.aggiungiLibro(libro), "L'aggiunta di un libro valido non dovrebbe lanciare eccezioni");

        Libro trovato = controller.cercaLibro(isbn);
        assertNotNull(trovato, "Il libro dovrebbe essere stato trovato nel DB");
        assertEquals("Lo scudo di Talos", trovato.getTitolo());

        // Cleanup
        controller.rimuoviLibro(isbn);
    }

    @Test
    void testAggiungiLibro_ISBNDuplicato() {
        String isbn = "978-0261102385"; // ISBN reale de "Il Signore degli Anelli"
        Libro libro1 = new Libro(isbn, "Il Signore degli Anelli", "J.R.R. Tolkien", 3, 3);
        Libro libro2 = new Libro(isbn, "Il Signore degli Anelli", "Tolkien", 1, 1); // Tentativo duplicato

        // Pulizia preventiva
        try {
            controller.rimuoviLibro(isbn);
        } catch (Exception ignored) {
        }

        controller.aggiungiLibro(libro1);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.aggiungiLibro(libro2);
        });

        assertEquals("Libro con ISBN 978-0261102385 già presente nel catalogo.", e.getMessage());

        // Cleanup
        controller.rimuoviLibro(isbn);
    }

    @Test
    void testModificaLibro_Success() {
        String isbn = "978-8806218294";
        Libro libro = new Libro(isbn, "1984", "George Orwell", 10, 10);

        // Pulizia preventiva
        try {
            controller.rimuoviLibro(isbn);
        } catch (Exception ignored) {
        }

        controller.aggiungiLibro(libro);

        Libro modificato = new Libro(isbn, "1984 (Edizione Speciale)", "George Orwell", 12, 12);
        controller.modificaLibro(modificato);

        // Assert
        Libro verificato = controller.cercaLibro(isbn);
        assertNotNull(verificato);
        assertEquals("1984 (Edizione Speciale)", verificato.getTitolo());
        assertEquals(12, verificato.getCopieTotali());

        // Cleanup
        controller.rimuoviLibro(isbn);
    }

    @Test
    void testRimuoviLibro_ConPrestitiAttivi() {
        String isbn = "978-8807013939";
        Libro libro = new Libro(isbn, "Seta", "Alessandro Baricco", 5, 3);

        // Pulizia preventiva
        try {
            controller.rimuoviLibro(isbn);
        } catch (Exception ignored) {
        }
        controller.aggiungiLibro(libro);


        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.rimuoviLibro(isbn);
        });

        assertEquals("Impossibile eliminare: ci sono 2 copie in prestito.", e.getMessage());

        // Cleanup: Per eliminare, devo prima "restituire" le copie
        Libro modificato = new Libro(isbn, "Seta", "Alessandro Baricco", 5, 5); // Ora disponibili = totali
        controller.modificaLibro(modificato);

        assertDoesNotThrow(() -> controller.rimuoviLibro(isbn));
    }
}