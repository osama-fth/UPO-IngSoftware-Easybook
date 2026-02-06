package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Bibliotecario.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class Bibliotecario {
    private final String nome;
    private final String cognome;

    public Bibliotecario(String matricola, String nome, String cognome, String username, String password) {
        this.nome = nome;
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }
}
