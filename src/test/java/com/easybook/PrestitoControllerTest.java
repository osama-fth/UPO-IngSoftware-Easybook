package com.easybook;

import com.easybook.controller.PrestitoController;
import com.easybook.controller.UtenteController;
import com.easybook.controller.CatalogoController;
import com.easybook.model.Libro;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PrestitoControllerTest {

    private PrestitoController prestitoController;
    private UtenteController utenteController;
    private CatalogoController catalogoController;
    private Random random;

    @BeforeEach
    void setUp() {
        prestitoController = new PrestitoController();
        utenteController = new UtenteController();
        catalogoController = new CatalogoController();
        random = new Random();
    }

    private String generaIsbnRandom() {
        return "ISBN-88-" + (10000 + random.nextInt(90000));
    }

    private String generaCfRandom() {
        return "MRARSS80A01-" + (100 + random.nextInt(899));
    }

    @Test
    void testRegistraPrestito_Successo() {
        String isbn = generaIsbnRandom();
        String cf = generaCfRandom();

        Utente utente = new Utente(cf, "Mario", "Rossi", StatoUtente.ATTIVO, 0);
        Libro libro = new Libro(isbn, "Il nome della rosa", "Umberto Eco", 5, 5);

        utenteController.registraUtente(utente);
        catalogoController.aggiungiLibro(libro);

        assertDoesNotThrow(() -> prestitoController.registraPrestito(cf, isbn));

        Libro libroAggiornato = catalogoController.cercaLibro(isbn);
        assertEquals(4, libroAggiornato.getCopieDisponibili());
    }

    @Test
    void testRegistraPrestito_UtenteSospeso() {
        String isbn = generaIsbnRandom();
        String cf = generaCfRandom();

        Utente utente = new Utente(cf, "Luca", "Verdi", StatoUtente.SOSPESO, 0);
        Libro libro = new Libro(isbn, "1984", "George Orwell", 2, 2);

        utenteController.registraUtente(utente);
        catalogoController.aggiungiLibro(libro);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                prestitoController.registraPrestito(cf, isbn)
        );

        assertTrue(e.getMessage().contains("è SOSPESO"));
    }

    @Test
    void testRegistraPrestito_LimiteSuperato() {
        String cf = generaCfRandom();
        Utente utente = new Utente(cf, "Sara", "Neri", StatoUtente.ATTIVO, 0);
        utenteController.registraUtente(utente);

        for (int i = 1; i <= 3; i++) {
            String tempIsbn = generaIsbnRandom();
            Libro l = new Libro(tempIsbn, "Libro " + i, "Autore", 1, 1);
            catalogoController.aggiungiLibro(l);
            prestitoController.registraPrestito(cf, tempIsbn);
        }

        String isbnSforato = generaIsbnRandom();
        Libro libro4 = new Libro(isbnSforato, "Il quarto libro", "Autore", 1, 1);
        catalogoController.aggiungiLibro(libro4);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                prestitoController.registraPrestito(cf, isbnSforato)
        );

        assertTrue(e.getMessage().contains("limite massimo"));
    }

    @Test
    void testRegistraPrestito_LibroEsaurito() {
        String isbn = generaIsbnRandom();
        String cf = generaCfRandom();

        Utente utente = new Utente(cf, "Anna", "Bianchi", StatoUtente.ATTIVO, 0);
        Libro libro = new Libro(isbn, "Libro Esaurito", "Autore", 1, 0);

        utenteController.registraUtente(utente);
        catalogoController.aggiungiLibro(libro);

        Exception e = assertThrows(IllegalArgumentException.class, () ->
                prestitoController.registraPrestito(cf, isbn)
        );

        assertEquals("Nessuna copia disponibile per il libro con ISBN " + isbn + ".", e.getMessage());
    }
}