package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.Libro;
import com.easybook.model.Prestito;
import com.easybook.model.Utente;

import java.util.List;

/**
 * Controller per la gestione del prestito (UC3 & UC4).
 *
 * @author Bellotti Lorenzo 20054630
 */
public class PrestitoController {

    private final UtenteDAO utenteDAO;
    private final LibroDAO libroDAO;
    private final PrestitoDAO prestitoDAO;

    public PrestitoController() {
        this.utenteDAO = new UtenteDAO();
        this.libroDAO = new LibroDAO();
        this.prestitoDAO = new PrestitoDAO();
    }

    public void registraPrestito(String cfUtente, String isbnLibro) {
        Utente utente = utenteDAO.findByCf(cfUtente);
        if (utente == null) {
            throw new IllegalArgumentException("Utente con CF " + cfUtente + " non trovato.");
        }
        if (utente.getStato().equals("SOSPESO")) {
            throw new IllegalArgumentException("Utente con CF " + cfUtente + " è SOSPESO.");
        }

        int prestitiAttivi = prestitoDAO.countPrestitiAttivi(cfUtente);
        if (prestitiAttivi >= 3) {
            throw new IllegalArgumentException(
                    "Utente con CF " + cfUtente + " ha raggiunto il limite massimo di prestiti attivi.");
        }

        Libro libro = libroDAO.findByIsbn(isbnLibro);
        if (libro == null) {
            throw new IllegalArgumentException("Libro con ISBN " + isbnLibro + " non trovato.");
        }
        if (libro.getCopieDisponibili() <= 0) {
            throw new IllegalArgumentException("Nessuna copia disponibile per il libro con ISBN " + isbnLibro + ".");
        }

        Prestito prestito = new Prestito(utente, libro);
        prestitoDAO.insert(prestito);
        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() - 1);
        utente.setNumPrestitiAttivi(utente.getNumPrestitiAttivi() + 1);
        utenteDAO.update(utente);
    }

    public List<Prestito> getTuttiIPrestiti() {
        return prestitoDAO.findAll();
    }
}
