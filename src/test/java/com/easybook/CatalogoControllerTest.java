package com.easybook;

import com.easybook.controller.CatalogoController;
import com.easybook.model.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per il controller del catalogo libri (UC1).
 *
 * @author Foutih Osama 20054809
 */
class CatalogoControllerTest extends TestBase {

    private CatalogoController controller;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp(); // Inizializza il DB in memoria
        controller = new CatalogoController(); // Crea il controller DOPO il setup del DB
    }

    @Test
    void testAggiungiLibro_Success() {
        String isbn = "978-8804669863";
        Libro libro = new Libro(isbn, "Lo scudo di Talos", "Valerio Massimo Manfredi", 3, 3);

        assertDoesNotThrow(() -> controller.aggiungiLibro(libro));

        Libro trovato = controller.cercaLibro(isbn);
        assertNotNull(trovato, "Il libro dovrebbe essere stato trovato nel DB");
        assertEquals("Lo scudo di Talos", trovato.getTitolo());
    }

    @Test
    void testAggiungiLibro_ISBNDuplicato() {
        String isbn = "978-0261102385";
        Libro libro1 = new Libro(isbn, "Il Signore degli Anelli", "J.R.R. Tolkien", 3, 3);
        Libro libro2 = new Libro(isbn, "The Lord of the Rings", "Tolkien", 1, 1);

        controller.aggiungiLibro(libro1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.aggiungiLibro(libro2);
        });

        assertTrue(exception.getMessage().contains("già presente"));
    }

    @Test
    void testModificaLibro_Success() {
        String isbn = "978-8806218294";
        Libro libro = new Libro(isbn, "1984", "George Orwell", 10, 10);
        controller.aggiungiLibro(libro);

        Libro modificato = new Libro(isbn, "1984 (Edizione Speciale)", "George Orwell", 12, 12);
        controller.modificaLibro(modificato);

        Libro verificato = controller.cercaLibro(isbn);
        assertNotNull(verificato);
        assertEquals("1984 (Edizione Speciale)", verificato.getTitolo());
        assertEquals(12, verificato.getCopieTotali());
    }

    @Test
    void testRimuoviLibro_ConPrestitiAttivi() {
        String isbn = "978-8807013939";
        Libro libro = new Libro(isbn, "Seta", "Alessandro Baricco", 5, 3);
        controller.aggiungiLibro(libro);

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            controller.rimuoviLibro(isbn);
        });

        assertTrue(e.getMessage().contains("copie"));

        Libro libero = new Libro(isbn, "Seta", "Alessandro Baricco", 5, 5);
        controller.modificaLibro(libero);

        assertDoesNotThrow(() -> controller.rimuoviLibro(isbn));
    }
}
