package com.easybook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce la connessione al database SQLite implementando il pattern Singleton.
 *
 * @author Foutih Osama 20054809
 * @author Lorezo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private final Connection connection;

    private DatabaseManager() {
        try {
            String URL = "jdbc:sqlite:easybook.db";
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Errore di connessione al DB: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        } else {
            try {
                // Se la connessione è chiusa, la riapriamo
                if (instance.getConnection().isClosed()) {
                    instance = new DatabaseManager();
                }
            } catch (SQLException e) {
                throw new RuntimeException("Errore durante la verifica della connessione: " + e.getMessage());
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}