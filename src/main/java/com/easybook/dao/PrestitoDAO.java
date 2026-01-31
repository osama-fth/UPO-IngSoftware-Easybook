package com.easybook.dao;

import com.easybook.model.Prestito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Bellotti Lorenzo 20054630
 */

public class PrestitoDAO {

    public void insert(Prestito prestito){
        String sql = "INSERT INTO Prestito(utente_cf, libro_isbn, data_inizio, data_scadenza, data_restituzione) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, prestito.getUtente().getCf());
            pstmt.setString(2, prestito.getLibro().getIsbn());

            pstmt.setString(3, prestito.getDataInizio().toString());
            pstmt.setString(4, prestito.getDataScadenza().toString());

            if (prestito.getDataRestituzione() != null) {
                pstmt.setString(5, prestito.getDataRestituzione().toString());
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento del prestito", e);
        }
    }

    public int countPrestitiAttivi(String cfUtente) {
        String sql = "SELECT COUNT(*) AS num_prestiti_attivi FROM Prestito WHERE utente_cf = ? AND data_restituzione IS NULL";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cfUtente);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("num_prestiti_attivi");
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il conteggio dei prestiti attivi per l'utente con CF " + cfUtente, e);
        }
    }

    public boolean findActiveByLibro(String isbn) {
        String sql = "SELECT * FROM Prestito WHERE libro_isbn = ? AND data_restituzione IS NULL";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca dei prestiti attivi per il libro con ISBN " + isbn, e);
        }
    }
}
