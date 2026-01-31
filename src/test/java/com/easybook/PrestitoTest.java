package com.easybook;

import com.easybook.model.Libro;
import com.easybook.model.Prestito;
import com.easybook.model.Utente;
import com.easybook.model.StatoUtente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PrestitoTest {

    private Utente utente;
    private Libro libro;
    private LocalDate dataInizio;

    @BeforeEach
    void setUp() {
        utente = new Utente("RSSMRA80A01H501U", "Mario", "Rossi", StatoUtente.ATTIVO, 0);
        libro = new Libro("978-8804668237", "Il nome della rosa", "Umberto Eco", 5, 5);
        dataInizio = LocalDate.now();
    }

    @Test
    void testCreazionePrestito() {
        Prestito prestito = new Prestito(1, utente, libro, dataInizio);

        assertEquals(1, prestito.getId());
        assertEquals("RSSMRA80A01H501U", prestito.getUtente().getCf());
        assertEquals("978-8804668237", prestito.getLibro().getIsbn());

        assertEquals(dataInizio, prestito.getDataInizio());
        assertEquals(dataInizio.plusDays(30), prestito.getDataScadenza());
        assertNull(prestito.getDataRestituzione(), "La data di restituzione deve essere null all'inizio");
    }

    @Test
    void testSettersPrestito() {
        Prestito prestito = new Prestito(1, utente, libro, dataInizio);
        LocalDate nuovaData = dataInizio.plusDays(1);
        LocalDate dataRestituzione = dataInizio.plusDays(15);

        prestito.setId(99);
        prestito.setDataInizio(nuovaData);
        prestito.setDataRestituzione(dataRestituzione);

        assertEquals(99, prestito.getId());
        assertEquals(nuovaData, prestito.getDataInizio());
        assertEquals(dataRestituzione, prestito.getDataRestituzione());
    }

    @Test
    void testCoerenzaStatoUtenteNelPrestito() {
        Prestito prestito = new Prestito(1, utente, libro, dataInizio);

        assertEquals("ATTIVO", prestito.getUtente().getStato());
    }
}