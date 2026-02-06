package com.easybook;

import com.easybook.dao.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Classe per connettersi a database temporareo per i test
 *
 * @author Foutih Osama 20054809
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class TestBase {

    @BeforeEach
    public void setUp() throws Exception {
        // Usa un database in memoria per isolamento totale e velocità
        String testUrl = "jdbc:sqlite::memory:";
        DatabaseManager.setTestInstance(testUrl);

        Connection conn = DatabaseManager.getInstance().getConnection();
        Statement stmt = conn.createStatement();

        String sqlScript = loadResourceFile();
        String[] queries = sqlScript.split(";");

        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                stmt.execute(query);
            }
        }

        stmt.close();
    }

    private String loadResourceFile() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("DDL.sql");
        if (is == null) {
            throw new RuntimeException("Impossibile trovare il file: DDL.sql");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la lettura del file DDL.sql", e);
        }
    }
}
