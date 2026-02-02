package com.easybook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestisce la connessione al database SQLite implementando il pattern Singleton.
 *
 * @author Foutih Osama 20054809
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private String url = "jdbc:sqlite:easybook.db";

    private DatabaseManager() {
        connetti();
    }

    private void connetti() {
        try {
            this.connection = DriverManager.getConnection(url);
            // Abilita le foreign keys per SQLite (fondamentale per i vincoli ON DELETE CASCADE)
            try (Statement stmt = this.connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore critico di connessione al DB: " + e.getMessage(), e);
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        } else {
            try {
                if (instance.connection == null || instance.connection.isClosed()) {
                    instance = new DatabaseManager();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * METODO PER I TEST: Imposta un database temporaneo.
     */
    public static void setTestInstance(String testUrl) {
        if (instance != null && instance.connection != null) {
            try {
                instance.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        instance = new DatabaseManager();
        instance.url = testUrl;
        instance.connetti();
    }
}