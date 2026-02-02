package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.model.Libro;

import java.util.List;

/**
 * Controller per la gestione del catalogo libri (UC1).
 *
 * @author Foutih Osama 20054809
 */
public class CatalogoController {

    private final LibroDAO libroDAO;

    public CatalogoController() {
        this.libroDAO = new LibroDAO();
    }

    public void aggiungiLibro(Libro libro) {
        // Controllo duplicati
        Libro esistente = libroDAO.findByIsbn(libro.getIsbn());
        if (esistente != null) {
            throw new IllegalArgumentException("Errore: Libro con ISBN " + libro.getIsbn() + " già presente nel catalogo.");
        }

        validareDatiCopie(libro);

        libroDAO.insert(libro);
    }

    public void modificaLibro(Libro libro) {
        Libro esistente = libroDAO.findByIsbn(libro.getIsbn());
        if (esistente == null) {
            throw new IllegalArgumentException("Errore: Libro con ISBN " + libro.getIsbn() + " non trovato.");
        }

        validareDatiCopie(libro);

        libroDAO.update(libro);
    }

    public void rimuoviLibro(String isbn) {
        Libro libro = libroDAO.findByIsbn(isbn);
        if (libro == null) {
            throw new IllegalArgumentException("Errore: Libro con ISBN " + isbn + " non trovato.");
        }

        // Verifica che non ci siano copie in prestito
        int copiePrestitate = libro.getCopieTotali() - libro.getCopieDisponibili();
        if (copiePrestitate > 0) {
            throw new IllegalArgumentException(
                    "Impossibile eliminare: ci sono " + copiePrestitate + " copie ancora in prestito.");
        }

        libroDAO.delete(isbn);
    }

    public List<Libro> getTuttiILibri() {
        return libroDAO.findAll();
    }

    public Libro cercaLibro(String isbn) {
        return libroDAO.findByIsbn(isbn);
    }

    private void validareDatiCopie(Libro libro) {
        if (libro.getCopieTotali() < 0 || libro.getCopieDisponibili() < 0) {
            throw new IllegalArgumentException("Il numero di copie non può essere negativo.");
        }

        if (libro.getCopieDisponibili() > libro.getCopieTotali()) {
            throw new IllegalArgumentException("Le copie disponibili non possono superare quelle totali.");
        }
    }
}