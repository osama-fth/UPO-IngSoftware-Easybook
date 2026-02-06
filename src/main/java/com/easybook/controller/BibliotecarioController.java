package com.easybook.controller;

import com.easybook.dao.BibliotecarioDAO;
import com.easybook.model.Bibliotecario;

/**
 * Controller per la gestione dell'autenticazione del bibliotecario.
 *
 * @author Bellotti Lorenzo 20054630
 * @author Negrini Riccardo 20054675
 */
public class BibliotecarioController {

    private static Bibliotecario bibliotecarioCorrente = null;
    private final BibliotecarioDAO bibliotecarioDAO;

    public BibliotecarioController() {
        this.bibliotecarioDAO = new BibliotecarioDAO();
    }

    public static Bibliotecario getBibliotecarioCorrente() {
        return bibliotecarioCorrente;
    }

    public static boolean isAutenticato() {
        return bibliotecarioCorrente != null;
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
}
