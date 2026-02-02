package com.easybook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Foutih Osama 20054809
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private String url = "jdbc:sqlite:easybook.db";

    private DatabaseManager() {
        // Costruttore privato per singleton
    }

    private DatabaseManager(String customUrl) {
        this.url = customUrl;
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            instance.connetti();
        }
        return instance;
    }

    /**
     * Imposta un'istanza di test con URL personalizzato (es. jdbc:sqlite::memory:)
     */
    public static synchronized void setTestInstance(String testUrl) {
        // Chiudi la connessione esistente se presente
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                // Ignora errori di chiusura
            }
        }

        // Crea nuova istanza con URL di test
        instance = new DatabaseManager(testUrl);
        instance.connetti();
    }

    /**
     * Resetta l'istanza singleton (utile per i test)
     */
    public static synchronized void resetInstance() {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                // Ignora errori di chiusura
            }
        }
        instance = null;
    }

    private void connetti() {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException("Errore di connessione al database: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        try {
            // Riconnetti se la connessione è chiusa
            if (connection == null || connection.isClosed()) {
                connetti();
            }
        } catch (SQLException e) {
            connetti();
        }
        return connection;
    }
}
