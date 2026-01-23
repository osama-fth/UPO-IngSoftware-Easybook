package com.easybook.controller;

import com.easybook.dao.UtenteDAO;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;

import java.util.List;

/**
 * Controller per la gestione dell'anagrafica utenti (UC2).
 * Implementa la logica di business senza dipendenze dalla GUI.
 */
public class UtenteController {

    private final UtenteDAO utenteDAO;

    public UtenteController() {
        this.utenteDAO = new UtenteDAO();
    }

    /**
     * Registra un nuovo utente.
     *
     * @throws IllegalArgumentException se il CF è già presente
     */
    public void registraUtente(Utente utente) {
        // Controllo duplicati
        Utente esistente = utenteDAO.findByCf(utente.getCf());
        if (esistente != null) {
            throw new IllegalArgumentException("Utente con CF " + utente.getCf() + " già registrato.");
        }

        // Validazioni base
        if (utente.getCf() == null || utente.getCf().trim().isEmpty()) {
            throw new IllegalArgumentException("Il codice fiscale è obbligatorio.");
        }

        if (utente.getNome() == null || utente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome è obbligatorio.");
        }

        if (utente.getCognome() == null || utente.getCognome().trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome è obbligatorio.");
        }

        utenteDAO.insert(utente);
    }

    /**
     * Modifica i dati di un utente esistente.
     *
     * @throws IllegalArgumentException se l'utente non esiste
     */
    public void modificaUtente(Utente utente) {
        Utente esistente = utenteDAO.findByCf(utente.getCf());
        if (esistente == null) {
            throw new IllegalArgumentException("Utente con CF " + utente.getCf() + " non trovato.");
        }

        utenteDAO.update(utente);
    }

    /**
     * Rimuove un utente dall'anagrafica.
     *
     * @throws IllegalArgumentException se l'utente ha prestiti attivi
     */
    public void rimuoviUtente(String cf) {
        Utente utente = utenteDAO.findByCf(cf);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con CF " + cf + " non trovato.");
        }

        if (utente.getNumPrestitiAttivi() > 0) {
            throw new IllegalArgumentException(
                    "Impossibile eliminare: l'utente ha " + utente.getNumPrestitiAttivi() + " prestiti attivi.");
        }

        utenteDAO.delete(cf);
    }

    /**
     * Recupera tutti gli utenti registrati.
     */
    public List<Utente> getElencoUtenti() {
        return utenteDAO.findAll();
    }

    /**
     * Cerca un utente per codice fiscale.
     */
    public Utente cercaUtente(String cf) {
        return utenteDAO.findByCf(cf);
    }

    /**
     * Sospende un utente (es. per sanzioni non pagate).
     */
    public void sospendiUtente(String cf) {
        Utente utente = utenteDAO.findByCf(cf);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con CF " + cf + " non trovato.");
        }

        utenteDAO.updateStato(cf, StatoUtente.SOSPESO);
    }

    /**
     * Riattiva un utente sospeso.
     */
    public void riattivaUtente(String cf) {
        Utente utente = utenteDAO.findByCf(cf);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con CF " + cf + " non trovato.");
        }

        utenteDAO.updateStato(cf, StatoUtente.ATTIVO);
    }
}
