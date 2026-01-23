package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.model.Libro;

import java.util.List;

/**
 * Controller per la gestione del catalogo libri (UC1).
 */
public class CatalogoController {

    private final LibroDAO libroDAO;

    public CatalogoController() {
        this.libroDAO = new LibroDAO();
    }

    /**
     * Aggiunge un nuovo libro al catalogo.
     *
     * @throws IllegalArgumentException se l'ISBN è già presente
     */
    public void aggiungiLibro(Libro libro) {
        // Controllo duplicati
        Libro esistente = libroDAO.findByIsbn(libro.getIsbn());
        if (esistente != null) {
            throw new IllegalArgumentException("Libro con ISBN " + libro.getIsbn() + " già presente nel catalogo.");
        }

        // Validazioni base
        if (libro.getCopieTotali() < 0 || libro.getCopieDisponibili() < 0) {
            throw new IllegalArgumentException("Il numero di copie non può essere negativo.");
        }

        if (libro.getCopieDisponibili() > libro.getCopieTotali()) {
            throw new IllegalArgumentException("Le copie disponibili non possono superare quelle totali.");
        }

        libroDAO.insert(libro);
    }

    /**
     * Modifica un libro esistente.
     *
     * @throws IllegalArgumentException se il libro non esiste
     */
    public void modificaLibro(Libro libro) {
        Libro esistente = libroDAO.findByIsbn(libro.getIsbn());
        if (esistente == null) {
            throw new IllegalArgumentException("Libro con ISBN " + libro.getIsbn() + " non trovato.");
        }

        if (libro.getCopieDisponibili() > libro.getCopieTotali()) {
            throw new IllegalArgumentException("Le copie disponibili non possono superare quelle totali.");
        }

        libroDAO.update(libro);
    }

    /**
     * Rimuove un libro dal catalogo.
     *
     * @throws IllegalArgumentException se il libro non esiste o ha prestiti attivi
     */
    public void rimuoviLibro(String isbn) {
        Libro libro = libroDAO.findByIsbn(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("Libro con ISBN " + isbn + " non trovato.");
        }

        // Verifica che non ci siano copie in prestito
        int copiePrestitate = libro.getCopieTotali() - libro.getCopieDisponibili();
        if (copiePrestitate > 0) {
            throw new IllegalArgumentException(
                    "Impossibile eliminare: ci sono " + copiePrestitate + " copie in prestito.");
        }

        libroDAO.delete(isbn);
    }

    /**
     * Recupera tutti i libri del catalogo.
     */
    public List<Libro> getTuttiILibri() {
        return libroDAO.findAll();
    }

    /**
     * Cerca un libro per ISBN.
     */
    public Libro cercaLibro(String isbn) {
        return libroDAO.findByIsbn(isbn);
    }
}
