package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Bibliotecario.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
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
}