package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Utente.
 *
 * @author Foutih Osama 20054809
 */
public class Utente {
    private final String cf;
    private final String nome;
    private final String cognome;
    private StatoUtente stato;
    private int numPrestitiAttivi;

    public Utente(String cf, String nome, String cognome, StatoUtente stato, int numPrestitiAttivi) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.stato = stato;
        this.numPrestitiAttivi = numPrestitiAttivi;
    }

    public String getCf() {
        return cf;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getStato() {
        return stato.toString();
    }

    public void setStato(StatoUtente stato) {
        this.stato = stato;
    }

    public int getNumPrestitiAttivi() {
        return numPrestitiAttivi;
    }

    public void setNumPrestitiAttivi(int numPrestitiAttivi) {
        this.numPrestitiAttivi = numPrestitiAttivi;
    }
}