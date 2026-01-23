package com.easybook;

import com.easybook.dao.DatabaseManager;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testConnectionIsNotNull() {
        DatabaseManager dbManager = DatabaseManager.getInstance();
        assertNotNull(dbManager, "L'istanza di DatabaseManager non dovrebbe essere null");
    }

    @Test
    void testConnectionIsOpen() throws SQLException {
        Connection conn = DatabaseManager.getInstance().getConnection();
        assertNotNull(conn, "La connessione SQL non dovrebbe essere null");
        assertFalse(conn.isClosed(), "La connessione al database dovrebbe essere aperta");
    }

    @Test
    void testSingletonInstance() {
        DatabaseManager instance1 = DatabaseManager.getInstance();
        DatabaseManager instance2 = DatabaseManager.getInstance();
        assertSame(instance1, instance2, "Le due istanze devono essere identiche (Pattern Singleton)");
    }
}