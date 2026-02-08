package com.easybook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Lorenzo Bellotti 20054630
 */
class PrestitoTest {

    private Utente utente;
    private Libro libro;

    @BeforeEach
    void setUp() {
        utente = new Utente("RSSMRA80A01H501U", "Mario", "Rossi", StatoUtente.ATTIVO, 0);
        libro = new Libro("978-8804668237", "Il nome della rosa", "Umberto Eco", 5, 5);
    }

    @Test
    void testCreazionePrestito() {
        Prestito prestito = new Prestito(utente, libro);

        assertEquals("RSSMRA80A01H501U", prestito.getUtente().getCf());
        assertEquals("978-8804668237", prestito.getLibro().getIsbn());

        LocalDate oggi = LocalDate.now();
        assertEquals(oggi, prestito.getDataInizio(), "La data inizio deve essere quella odierna");
        assertEquals(oggi.plusDays(30), prestito.getDataScadenza(), "La scadenza deve essere a 30 giorni");
        assertNull(prestito.getDataRestituzione(), "La data di restituzione deve essere null alla creazione");
    }

    @Test
    void testSettersPrestito() {
        Prestito prestito = new Prestito(utente, libro);

        LocalDate dataPersonalizzata = LocalDate.of(2024, 1, 1);
        LocalDate dataRestituzione = LocalDate.now();

        prestito.setId(10);
        prestito.setDataInizio(dataPersonalizzata);
        prestito.setDataRestituzione(dataRestituzione);

        assertEquals(10, prestito.getId());
        assertEquals(dataPersonalizzata, prestito.getDataInizio());
        assertEquals(dataRestituzione, prestito.getDataRestituzione());
    }

    @Test
    void testCoerenzaStatoUtenteNelPrestito() {
        Prestito prestito = new Prestito(utente, libro);

        assertEquals("ATTIVO", prestito.getUtente().getStato());
    }
}