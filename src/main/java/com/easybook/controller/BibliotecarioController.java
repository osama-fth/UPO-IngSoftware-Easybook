package com.easybook.controller;

import com.easybook.dao.BibliotecarioDAO;
import com.easybook.model.Bibliotecario;

/**
 * Controller per la gestione dell'autenticazione del bibliotecario.
 *
 * @author Foutih Osama 20054809
 */
public class BibliotecarioController {

    private final BibliotecarioDAO bibliotecarioDAO;
    private static Bibliotecario bibliotecarioCorrente = null;

    public BibliotecarioController() {
        this.bibliotecarioDAO = new BibliotecarioDAO();
    }

    public Bibliotecario login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username obbligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password obbligatoria");
        }

        Bibliotecario bibliotecario = bibliotecarioDAO.login(username, password);

        if (bibliotecario != null) {
            bibliotecarioCorrente = bibliotecario;
        }

        return bibliotecario;
    }

    public void logout() {
        bibliotecarioCorrente = null;
    }

    public static Bibliotecario getBibliotecarioCorrente() {
        return bibliotecarioCorrente;
    }
    
    public static boolean isAutenticato() {
        return bibliotecarioCorrente != null;
    }
}
