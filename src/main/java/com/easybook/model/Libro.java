package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Libro.
 *
 * @author Foutih Osama 20054809
 */
public class Libro {
    private final String isbn;
    private final String titolo;
    private final String autore;
    private final int copieTotali;
    private int copieDisponibili;

    public Libro(String isbn, String titolo, String autore, int copieTotali, int copieDisponibili) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.autore = autore;
        this.copieTotali = copieTotali;
        this.copieDisponibili = copieDisponibili;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    public int getCopieTotali() {
        return copieTotali;
    }

    public int getCopieDisponibili() {
        return copieDisponibili;
    }

    public void setCopieDisponibili(int copieDisponibili) {
        this.copieDisponibili = copieDisponibili;
    }
}