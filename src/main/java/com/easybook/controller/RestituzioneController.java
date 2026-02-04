package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.SanzioneDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.Libro;
import com.easybook.model.Prestito;
import com.easybook.model.Sanzione;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Controller per la gestione della restituzione (UC5) e sanzioni (UC6).
 *
 * @author Riccardo Negrini 20054675
 */
public class RestituzioneController {
    private final SanzioneDAO sanzioneDAO;
    private final PrestitoDAO prestitoDAO;
    private final LibroDAO libroDAO;
    private final UtenteDAO utenteDAO;

    public RestituzioneController() {
        this.sanzioneDAO = new SanzioneDAO();
        this.prestitoDAO = new PrestitoDAO();
        this.libroDAO = new LibroDAO();
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Registra la restituzione di un libro.
     * Se il prestito è in ritardo, applica una sanzione e sospende l'utente.
     *
     * @param idPrestito ID del prestito da chiudere
     * @throws IllegalArgumentException se il prestito non esiste o è già stato restituito
     */
    public void registraRestituzione(int idPrestito) {
        // Recupera il prestito dal database
        Prestito prestito = trovaPrestito(idPrestito);
        
        if (prestito == null) {
            throw new IllegalArgumentException("Prestito con ID " + idPrestito + " non trovato.");
        }

        if (prestito.getDataRestituzione() != null) {
            throw new IllegalArgumentException("Prestito con ID " + idPrestito + " già restituito in data " 
                + prestito.getDataRestituzione());
        }

        LocalDate oggi = LocalDate.now();
        prestito.setDataRestituzione(oggi);

        // Verifica se c'è ritardo e applica eventuale sanzione
        if (oggi.isAfter(prestito.getDataScadenza())) {
            applicaSanzione(prestito, oggi);
        }

        // Aggiorna il prestito nel database
        prestitoDAO.update(prestito);

        // Incrementa le copie disponibili del libro
        Libro libro = prestito.getLibro();
        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() + 1);

        // Decrementa il numero di prestiti attivi dell'utente
        Utente utente = prestito.getUtente();
        utente.setNumPrestitiAttivi(utente.getNumPrestitiAttivi() - 1);
        utenteDAO.update(utente);
    }

    /**
     * Applica una sanzione per ritardo nella restituzione.
     * Logica: 10€ fino a 7 giorni, poi 1€ per ogni giorno aggiuntivo.
     *
     * @param prestito il prestito in ritardo
     * @param dataRestituzione la data effettiva di restituzione
     */
    private void applicaSanzione(Prestito prestito, LocalDate dataRestituzione) {
        long giorniRitardo = ChronoUnit.DAYS.between(prestito.getDataScadenza(), dataRestituzione);
        
        double importo;
        if (giorniRitardo <= 7) {
            importo = 10.0;
        } else {
            importo = 10.0 + (giorniRitardo - 7);
        }

        // Crea e salva la sanzione
        Sanzione sanzione = new Sanzione(prestito.getId(), importo);
        sanzioneDAO.insert(sanzione);

        // Sospende l'utente
        Utente utente = prestito.getUtente();
        utente.setStato(StatoUtente.SOSPESO);
        utenteDAO.update(utente);
    }

    /**
     * Trova un prestito per ID cercando in tutti i prestiti.
     *
     * @param idPrestito ID del prestito da cercare
     * @return il prestito trovato o null
     */
    private Prestito trovaPrestito(int idPrestito) {
        return prestitoDAO.findAll().stream()
            .filter(p -> p.getId() == idPrestito)
            .findFirst()
            .orElse(null);
    }

    /**
     * Verifica se un prestito ha una sanzione associata.
     *
     * @param idPrestito ID del prestito
     * @return true se esiste una sanzione, false altrimenti
     */
    public boolean hasSanzione(int idPrestito) {
        return sanzioneDAO.findbyprestitoid(idPrestito) != null;
    }

    /**
     * Recupera la sanzione associata a un prestito.
     *
     * @param idPrestito ID del prestito
     * @return la sanzione o null se non esiste
     */
    public Sanzione getSanzione(int idPrestito) {
        return sanzioneDAO.findbyprestitoid(idPrestito);
    }
}
