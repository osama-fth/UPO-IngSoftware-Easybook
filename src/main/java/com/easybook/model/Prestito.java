package com.easybook.model;

import java.time.LocalDate;

/**
 * Classe POJO che rappresenta l'entità Prestito.
 *
 * @author Bellotti Lorenzo 20054630
 */

public class Prestito {
    private int id;
    private Utente utente;
    private Libro libro;
    private LocalDate dataInizio;
    private LocalDate dataScadenza;
    private LocalDate dataRestituzione;

    public Prestito(int id, Utente utente, Libro libro,  LocalDate dataInizio) {
        this.id = id;
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;
        this.dataScadenza = dataInizio.plusDays(30);
        this.dataRestituzione = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public LocalDate getDataRestituzione() {
        return dataRestituzione;
    }

    public void setDataRestituzione(LocalDate dataRestituzione) {
        this.dataRestituzione = dataRestituzione;
    }
}
