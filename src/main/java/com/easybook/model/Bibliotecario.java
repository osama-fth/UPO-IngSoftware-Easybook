package com.easybook.model;

/**
 * Classe POJO che rappresenta l'operatore Bibliotecario.
 *
 * @author Foutih Osama 20054809
 */
public class Bibliotecario {
    private String matricola;
    private String nome;
    private String cognome;
    private String username;
    private String password;

    public Bibliotecario(String matricola, String nome, String cognome, String username, String password) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.password = password;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}