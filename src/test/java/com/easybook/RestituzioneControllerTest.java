package com.easybook;

import com.easybook.controller.RestituzioneController;
import com.easybook.dao.LibroDAO;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per RestituzioneController (UC5 e UC6).
 *
 * @author Riccardo Negrini 20054675
 */
class RestituzioneControllerTest extends TestBase {

    private RestituzioneController restituzioneController;
    private PrestitoDAO prestitoDAO;
    private LibroDAO libroDAO;
    private UtenteDAO utenteDAO;

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        restituzioneController = new RestituzioneController();
        prestitoDAO = new PrestitoDAO();
        libroDAO = new LibroDAO();
        utenteDAO = new UtenteDAO();

        // Setup dati di test
        setupDatiTest();
    }

    private void setupDatiTest() {
        // Inserisci utente di test
        Utente utente = new Utente("TSTCF001", "Test", "User", StatoUtente.ATTIVO, 0);
        utenteDAO.insert(utente);

        // Inserisci libro di test
        Libro libro = new Libro("ISBN-TEST-001", "Libro Test", "Autore Test", 5, 5);
        libroDAO.insert(libro);
    }

    /**
     * Test UC5: Restituzione puntuale (senza ritardo).
     */
    @Test
    void testRestituzionePuntuale() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

        // Crea prestito con scadenza futura
        Prestito prestito = new Prestito(utente, libro,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(25),
                null);
        prestitoDAO.insert(prestito);

        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() - 1);
        utente.setNumPrestitiAttivi(1);
        utenteDAO.update(utente);

        int idPrestito = getLastPrestitoId();

        // Esegui restituzione
        restituzioneController.registraRestituzione(idPrestito);

        // Verifica: nessuna sanzione
        assertFalse(restituzioneController.hasSanzione(idPrestito));

        // Verifica: utente rimane ATTIVO
        Utente utenteAggiornato = utenteDAO.findByCf("TSTCF001");
        assertEquals(StatoUtente.ATTIVO, utenteAggiornato.getStatoEnum());

        // Verifica: copie ripristinate
        Libro libroAggiornato = libroDAO.findByIsbn("ISBN-TEST-001");
        assertEquals(5, libroAggiornato.getCopieDisponibili());
    }

    /**
     * Test UC6: Restituzione con ritardo <= 7 giorni (sanzione fissa 10€).
     */
    @Test
    void testRestituzioneRitardoBreve() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

        // Crea prestito scaduto 5 giorni fa
        Prestito prestito = new Prestito(utente, libro,
                LocalDate.now().minusDays(35),
                LocalDate.now().minusDays(5),
                null);
        prestitoDAO.insert(prestito);

        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() - 1);
        utente.setNumPrestitiAttivi(1);
        utenteDAO.update(utente);

        int idPrestito = getLastPrestitoId();

        restituzioneController.registraRestituzione(idPrestito);

        // Verifica: sanzione di 10€
        assertTrue(restituzioneController.hasSanzione(idPrestito));
        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertEquals(10.0, sanzione.getImporto());

        // Verifica: utente SOSPESO
        Utente utenteAggiornato = utenteDAO.findByCf("TSTCF001");
        assertEquals(StatoUtente.SOSPESO, utenteAggiornato.getStatoEnum());
    }

    /**
     * Test UC6: Restituzione con ritardo > 7 giorni (10€ + 0.50€/giorno extra).
     */
    @Test
    void testRestituzioneRitardoLungo() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

        // Crea prestito scaduto 10 giorni fa
        Prestito prestito = new Prestito(utente, libro,
                LocalDate.now().minusDays(40),
                LocalDate.now().minusDays(10),
                null);
        prestitoDAO.insert(prestito);

        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() - 1);
        utente.setNumPrestitiAttivi(1);
        utenteDAO.update(utente);

        int idPrestito = getLastPrestitoId();

        restituzioneController.registraRestituzione(idPrestito);

        // Verifica: sanzione = 10€ + (10-7) * 0.50€ = 11.50€
        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertEquals(11.50, sanzione.getImporto(), 0.01);
    }

    /**
     * Test calcolo importo sanzione.
     */
    @Test
    void testCalcoloImportoSanzione() {
        assertEquals(0.0, restituzioneController.calcolaImportoSanzione(0));
        assertEquals(10.0, restituzioneController.calcolaImportoSanzione(1));
        assertEquals(10.0, restituzioneController.calcolaImportoSanzione(7));
        assertEquals(10.5, restituzioneController.calcolaImportoSanzione(8));
        assertEquals(11.0, restituzioneController.calcolaImportoSanzione(9));
        assertEquals(15.0, restituzioneController.calcolaImportoSanzione(17)); // 10 + 10*0.5
    }

    /**
     * Test eccezione: prestito inesistente.
     */
    @Test
    void testRestituzionePrestitoInesistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            restituzioneController.registraRestituzione(99999);
        });
    }

    private int getLastPrestitoId() {
        return prestitoDAO.findAll().stream()
                .mapToInt(Prestito::getId)
                .max()
                .orElseThrow();
    }
}
