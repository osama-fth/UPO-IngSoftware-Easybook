package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Libro.
 *
 * @author Foutih Osama 20054809
 */
public class Libro {
    // Rimosso 'final' per permettere modifiche (UC1: Modifica Libro)
    private String isbn;
    private String titolo;
    private String autore;
    private int copieTotali;
    private int copieDisponibili;

    public Libro() {
    }

    public Libro(String isbn, String titolo, String autore, int copieTotali, int copieDisponibili) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.autore = autore;
        this.copieTotali = copieTotali;
        this.copieDisponibili = copieDisponibili;
    }

    // Getter
    public String getIsbn() {
        return isbn;
    }

    // Setter
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public int getCopieTotali() {
        return copieTotali;
    }

    public void setCopieTotali(int copieTotali) {
        this.copieTotali = copieTotali;
    }

    public int getCopieDisponibili() {
        return copieDisponibili;
    }

    public void setCopieDisponibili(int copieDisponibili) {
        this.copieDisponibili = copieDisponibili;
    }
}