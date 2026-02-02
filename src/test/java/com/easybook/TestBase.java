package com.easybook;

import com.easybook.dao.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class TestBase {

    @BeforeEach
    public void setUp() throws Exception {
        // Usa un database in memoria per isolamento totale e velocità
        String testUrl = "jdbc:sqlite::memory:";
        DatabaseManager.setTestInstance(testUrl);

        // IMPORTANTE: NON usare try-with-resources qui!
        // Con SQLite in memoria, chiudere la connessione distrugge il database.
        Connection conn = DatabaseManager.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        String sqlScript = loadResourceFile();
        String[] queries = sqlScript.split(";");

        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                stmt.execute(query);
            }
        }
        stmt.close(); // Chiudiamo solo lo statement, NON la connessione
    }

    private String loadResourceFile() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("DDL.sql");
        if (is == null) {
            throw new RuntimeException("Impossibile trovare il file: " + "DDL.sql");
        }
        return new BufferedReader(new InputStreamReader(is))
                .lines().collect(Collectors.joining("\n"));
    }
}
