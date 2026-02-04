package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Utente.
 *
 * @author Foutih Osama 20054809
 */
public class Utente {
    private String cf;
    private String nome;
    private String cognome;
    private StatoUtente stato;
    private int numPrestitiAttivi;

    public Utente(String cf, String nome, String cognome, StatoUtente stato, int prestitiAttivi) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.stato = stato;
        this.numPrestitiAttivi = prestitiAttivi;
    }

    // Getters e Setters
    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getStato() {
        return stato.name();
    }

    public void setStato(StatoUtente stato) {
        this.stato = stato;
    }

    public StatoUtente getStatoEnum() {
        return stato;
    }

    public int getNumPrestitiAttivi() {
        return numPrestitiAttivi;
    }

    public void setNumPrestitiAttivi(int prestitiAttivi) {
        this.numPrestitiAttivi = prestitiAttivi;
    }
}
