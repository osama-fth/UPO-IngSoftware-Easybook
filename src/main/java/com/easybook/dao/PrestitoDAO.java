package com.easybook.dao;

import com.easybook.model.Libro;
import com.easybook.model.Prestito;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lorenzo Bellotti 20054630
 */
public class PrestitoDAO {

    public void insert(Prestito prestito) {
        String sql = "INSERT INTO Prestito (utente_cf, libro_isbn, data_inizio, data_scadenza, data_restituzione) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, prestito.getUtente().getCf());
            pstmt.setString(2, prestito.getLibro().getIsbn());
            pstmt.setString(3, prestito.getDataInizio().toString());
            pstmt.setString(4, prestito.getDataScadenza().toString());
            pstmt.setString(5,
                    prestito.getDataRestituzione() != null ? prestito.getDataRestituzione().toString() : null);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento del prestito: " + e.getMessage(), e);
        }
    }

    public int countPrestitiAttivi(String cfUtente) {
        String sql = "SELECT COUNT(*) FROM Prestito WHERE utente_cf = ? AND data_restituzione IS NULL";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cfUtente);
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            return count;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il conteggio prestiti attivi: " + e.getMessage(), e);
        }
    }

    public List<Prestito> findAll() {
        List<Prestito> prestiti = new ArrayList<>();
        String sql = "SELECT p.*, u.nome as u_nome, u.cognome as u_cognome, u.stato as u_stato, u.num_prestiti_attivi, "
                +
                "l.titolo, l.autore, l.copie_totali, l.copie_disponibili " +
                "FROM Prestito p " +
                "JOIN Utente u ON p.utente_cf = u.cf " +
                "JOIN Libro l ON p.libro_isbn = l.isbn";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                prestiti.add(creaPrestito(rs));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero dei prestiti: " + e.getMessage(), e);
        }
        return prestiti;
    }

    public void update(Prestito prestito) {
        String sql = "UPDATE Prestito SET data_restituzione = ? WHERE id = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,
                    prestito.getDataRestituzione() != null ? prestito.getDataRestituzione().toString() : null);
            pstmt.setInt(2, prestito.getId());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento del prestito: " + e.getMessage(), e);
        }
    }

    public Prestito findById(int id) {
        String sql = "SELECT p.*, u.nome as u_nome, u.cognome as u_cognome, u.stato as u_stato, u.num_prestiti_attivi, "
                + "l.titolo, l.autore, l.copie_totali, l.copie_disponibili " +
                "FROM Prestito p " +
                "JOIN Utente u ON p.utente_cf = u.cf " +
                "JOIN Libro l ON p.libro_isbn = l.isbn " +
                "WHERE p.id = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            Prestito prestito = null;
            if (rs.next()) {
                prestito = creaPrestito(rs);
            }
            rs.close();
            pstmt.close();
            return prestito;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca del prestito con ID: " + id, e);
        }
    }

    private Prestito creaPrestito(ResultSet rs) throws SQLException {
        Utente utente = new Utente(
                rs.getString("utente_cf"),
                rs.getString("u_nome"),
                rs.getString("u_cognome"),
                StatoUtente.valueOf(rs.getString("u_stato")),
                rs.getInt("num_prestiti_attivi"));

        Libro libro = new Libro(
                rs.getString("libro_isbn"),
                rs.getString("titolo"),
                rs.getString("autore"),
                rs.getInt("copie_totali"),
                rs.getInt("copie_disponibili"));

        LocalDate dataInizio = LocalDate.parse(rs.getString("data_inizio"));
        LocalDate dataScadenza = LocalDate.parse(rs.getString("data_scadenza"));
        String dataRestStr = rs.getString("data_restituzione");
        LocalDate dataRestituzione = dataRestStr != null ? LocalDate.parse(dataRestStr) : null;

        return new Prestito(rs.getInt("id"), utente, libro, dataInizio, dataScadenza, dataRestituzione);
    }
}
