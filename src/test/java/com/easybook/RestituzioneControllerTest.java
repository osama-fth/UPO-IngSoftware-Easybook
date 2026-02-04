package com.easybook;

import com.easybook.controller.PrestitoController;
import com.easybook.controller.RestituzioneController;
import com.easybook.dao.*;
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
    private PrestitoController prestitoController;
    private PrestitoDAO prestitoDAO;
    private LibroDAO libroDAO;
    private UtenteDAO utenteDAO;
    private SanzioneDAO sanzioneDAO;
    
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        
        restituzioneController = new RestituzioneController();
        prestitoController = new PrestitoController();
        prestitoDAO = new PrestitoDAO();
        libroDAO = new LibroDAO();
        utenteDAO = new UtenteDAO();
        sanzioneDAO = new SanzioneDAO();
    }
    
    /**
     * Test UC5: Restituzione puntuale (senza ritardo).
     * Verifica che:
     * - Il libro torni disponibile
     * - Nessuna sanzione venga creata
     * - L'utente rimanga ATTIVO
     */
    @Test
    void testRestituzionePuntuale() {
        // Recupera dati dal database di test
        Libro libro = libroDAO.findByIsbn("978-8811811651");
        Utente utente = utenteDAO.findByCF("RSSMRA80A01H501U");
        
        int copieInizialiLibro = libro.getCopieDisponibili();
        int prestitiInizialiUtente = utente.getNumPrestitiAttivi();
        
        // Crea un prestito con scadenza futura
        LocalDate dataInizio = LocalDate.now();
        LocalDate dataScadenza = dataInizio.plusDays(30);
        
        Prestito prestito = new Prestito(
            utente, 
            libro, 
            dataInizio, 
            dataScadenza, 
            null
        );
        
        prestitoDAO.insert(prestito);
        
        // Aggiorna libro e utente come farebbe il PrestitoController
        libroDAO.updateCopie(libro.getIsbn(), copieInizialiLibro - 1);
        utente.setNumPrestitiAttivi(prestitiInizialiUtente + 1);
        utenteDAO.update(utente);
        
        // Recupera l'ID del prestito appena creato
        int idPrestito = prestitoDAO.findAll().stream()
            .filter(p -> p.getUtente().getCf().equals(utente.getCf()) 
                && p.getLibro().getIsbn().equals(libro.getIsbn())
                && p.getDataRestituzione() == null)
            .map(Prestito::getId)
            .findFirst()
            .orElseThrow();
        
        // AZIONE: Registra la restituzione puntuale
        restituzioneController.registraRestituzione(idPrestito);
        
        // VERIFICA: Il prestito è stato chiuso
        Prestito prestitoAggiornato = prestitoDAO.findAll().stream()
            .filter(p -> p.getId() == idPrestito)
            .findFirst()
            .orElseThrow();
        
        assertNotNull(prestitoAggiornato.getDataRestituzione());
        assertEquals(LocalDate.now(), prestitoAggiornato.getDataRestituzione());
        
        // VERIFICA: Le copie del libro sono aumentate
        Libro libroAggiornato = libroDAO.findByIsbn(libro.getIsbn());
        assertEquals(copieInizialiLibro, libroAggiornato.getCopieDisponibili());
        
        // VERIFICA: I prestiti attivi dell'utente sono diminuiti
        Utente utenteAggiornato = utenteDAO.findByCF(utente.getCf());
        assertEquals(prestitiInizialiUtente, utenteAggiornato.getNumPrestitiAttivi());
        
        // VERIFICA: Nessuna sanzione creata
        assertFalse(restituzioneController.hasSanzione(idPrestito));
        
        // VERIFICA: Utente rimane ATTIVO
        assertEquals(StatoUtente.ATTIVO, utenteAggiornato.getStato());
    }
    
    /**
     * Test UC6: Restituzione in ritardo (ritardo < 7 giorni).
     * Verifica che:
     * - Venga creata una sanzione di 10€
     * - L'utente venga sospeso
     */
    @Test
    void testRestituzioneInRitardoBreve() {
        // Recupera dati dal database di test
        Libro libro = libroDAO.findByIsbn("978-8804719137");
        Utente utente = utenteDAO.findByCF("BNCGNN90B02F205Z");
        
        int copieInizialiLibro = libro.getCopieDisponibili();
        
        // Crea un prestito con scadenza nel PASSATO (5 giorni fa)
        LocalDate dataInizio = LocalDate.now().minusDays(35);
        LocalDate dataScadenza = LocalDate.now().minusDays(5);
        
        Prestito prestito = new Prestito(
            utente, 
            libro, 
            dataInizio, 
            dataScadenza, 
            null
        );
        
        prestitoDAO.insert(prestito);
        
        // Aggiorna libro
        libroDAO.updateCopie(libro.getIsbn(), copieInizialiLibro - 1);
        utente.setNumPrestitiAttivi(utente.getNumPrestitiAttivi() + 1);
        utenteDAO.update(utente);
        
        int idPrestito = prestitoDAO.findAll().stream()
            .filter(p -> p.getUtente().getCf().equals(utente.getCf()) 
                && p.getLibro().getIsbn().equals(libro.getIsbn())
                && p.getDataRestituzione() == null)
            .map(Prestito::getId)
            .findFirst()
            .orElseThrow();
        
        // AZIONE: Registra la restituzione IN RITARDO
        restituzioneController.registraRestituzione(idPrestito);
        
        // VERIFICA: È stata creata una sanzione
        assertTrue(restituzioneController.hasSanzione(idPrestito));
        
        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertNotNull(sanzione);
        assertEquals(10.0, sanzione.getImporto()); // Ritardo < 7 giorni = 10€
        assertEquals(idPrestito, sanzione.getIdprestito());
        assertFalse(sanzione.isPagata());
        
        // VERIFICA: L'utente è stato SOSPESO
        Utente utenteAggiornato = utenteDAO.findByCF(utente.getCf());
        assertEquals(StatoUtente.SOSPESO, utenteAggiornato.getStato());
    }
    
    /**
     * Test UC6: Restituzione con ritardo superiore a 7 giorni.
     * Verifica che la sanzione sia 10€ + 1€/giorno extra.
     */
    @Test
    void testRestituzioneInRitardoLungo() {
        // Recupera dati dal database di test
        Libro libro = libroDAO.findByIsbn("978-8869183157");
        Utente utente = utenteDAO.findByCF("RSSMRA80A01H501U");
        
        int copieInizialiLibro = libro.getCopieDisponibili();
        
        // Crea un prestito con scadenza 10 giorni fa
        LocalDate dataInizio = LocalDate.now().minusDays(40);
        LocalDate dataScadenza = LocalDate.now().minusDays(10);
        
        Prestito prestito = new Prestito(
            utente, 
            libro, 
            dataInizio, 
            dataScadenza, 
            null
        );
        
        prestitoDAO.insert(prestito);
        
        libroDAO.updateCopie(libro.getIsbn(), copieInizialiLibro - 1);
        utente.setNumPrestitiAttivi(utente.getNumPrestitiAttivi() + 1);
        utenteDAO.update(utente);
        
        int idPrestito = prestitoDAO.findAll().stream()
            .filter(p -> p.getUtente().getCf().equals(utente.getCf()) 
                && p.getLibro().getIsbn().equals(libro.getIsbn())
                && p.getDataRestituzione() == null)
            .map(Prestito::getId)
            .findFirst()
            .orElseThrow();
        
        // AZIONE: Registra la restituzione
        restituzioneController.registraRestituzione(idPrestito);
        
        // VERIFICA: Sanzione = 10€ + (10 - 7) = 13€
        Sanzione sanzione = restituzioneController.getSanzione(idPrestito);
        assertNotNull(sanzione);
        assertEquals(13.0, sanzione.getImporto()); // 10€ base + 3€ extra
        
        // VERIFICA: Utente sospeso
        Utente utenteAggiornato = utenteDAO.findByCF(utente.getCf());
        assertEquals(StatoUtente.SOSPESO, utenteAggiornato.getStato());
    }
    
    /**
     * Test eccezione: tentativo di restituire un prestito inesistente.
     */
    @Test
    void testRestituzionePrestitoInesistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            restituzioneController.registraRestituzione(99999);
        });
    }
    
    /**
     * Test eccezione: tentativo di restituire un prestito già restituito.
     */
    @Test
    void testRestituzionePrestitoGiaRestituito() {
        // Recupera un prestito già restituito dal database di test
        Prestito prestitoGiaRestituito = prestitoDAO.findAll().stream()
            .filter(p -> p.getDataRestituzione() != null)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Nessun prestito già restituito nel DB di test"));
        
        assertThrows(IllegalArgumentException.class, () -> {
            restituzioneController.registraRestituzione(prestitoGiaRestituito.getId());
        });
    }
}