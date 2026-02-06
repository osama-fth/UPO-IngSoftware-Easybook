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
 * Test per controller restituzione (UC5&UC6).
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

        setupDatiTest();
    }

    private void setupDatiTest() {
        Utente utente = new Utente("TSTCF001", "Test", "User", StatoUtente.ATTIVO, 0);
        utenteDAO.insert(utente);

        Libro libro = new Libro("ISBN-TEST-001", "Libro Test", "Autore Test", 5, 5);
        libroDAO.insert(libro);
    }

    @Test
    void testRestituzionePuntuale() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

        Prestito prestito = new Prestito(utente, libro,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusDays(25),
                null);
        prestitoDAO.insert(prestito);

        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() - 1);
        utente.setNumPrestitiAttivi(1);
        utenteDAO.update(utente);

        int idPrestito = getLastPrestitoId();

        restituzioneController.registraRestituzione(idPrestito);

        assertFalse(restituzioneController.hasSanzione(idPrestito));

        Utente utenteAggiornato = utenteDAO.findByCf("TSTCF001");
        assertEquals(StatoUtente.ATTIVO, utenteAggiornato.getStatoEnum());

        Libro libroAggiornato = libroDAO.findByIsbn("ISBN-TEST-001");
        assertEquals(5, libroAggiornato.getCopieDisponibili());
    }

    @Test
    void testRestituzioneRitardoBreve() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

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

        assertTrue(restituzioneController.hasSanzione(idPrestito));
        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertEquals(10.0, sanzione.getImporto());

        Utente utenteAggiornato = utenteDAO.findByCf("TSTCF001");
        assertEquals(StatoUtente.SOSPESO, utenteAggiornato.getStatoEnum());
    }

    @Test
    void testRestituzioneRitardoLungo() {
        Libro libro = libroDAO.findByIsbn("ISBN-TEST-001");
        Utente utente = utenteDAO.findByCf("TSTCF001");

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

        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertEquals(11.50, sanzione.getImporto(), 0.01);
    }

    @Test
    void testCalcoloImportoSanzione() {
        assertEquals(0.0, restituzioneController.calcolaImportoSanzione(0));
        assertEquals(10.0, restituzioneController.calcolaImportoSanzione(1));
        assertEquals(10.0, restituzioneController.calcolaImportoSanzione(7));
        assertEquals(10.5, restituzioneController.calcolaImportoSanzione(8));
        assertEquals(11.0, restituzioneController.calcolaImportoSanzione(9));
        assertEquals(15.0, restituzioneController.calcolaImportoSanzione(17)); // 10 + 10*0.5
    }

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
