package com.easybook.dao;

import com.easybook.model.Sanzione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione delle sanzioni nel database.
 *
 * @author Riccardo Negrini 20054675
 */
public class SanzioneDAO {

    public void insert(Sanzione sanzione) {
        String sql = "INSERT INTO Sanzione (prestito_id, importo, pagata) VALUES (?, ?, ?)";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sanzione.getIdprestito());
            pstmt.setDouble(2, sanzione.getImporto());
            pstmt.setInt(3, sanzione.isPagata() ? 1 : 0);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento della Sanzione: " + e.getMessage(), e);
        }
    }

    public Sanzione findByPrestitoId(int idPrestito) {
        String sql = "SELECT * FROM Sanzione WHERE prestito_id = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idPrestito);
            ResultSet rs = pstmt.executeQuery();
            Sanzione sanzione = null;
            if (rs.next()) {
                sanzione = new Sanzione(
                        rs.getInt("id"),
                        rs.getInt("prestito_id"),
                        rs.getDouble("importo"),
                        rs.getInt("pagata") == 1);
            }
            rs.close();
            pstmt.close();
            return sanzione;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca della Sanzione con ID_Prestito: " + idPrestito,
                    e);
        }
    }

    public List<Sanzione> findAll() {
        List<Sanzione> sanzioni = new ArrayList<>();
        String sql = "SELECT * FROM Sanzione";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                sanzioni.add(new Sanzione(
                        rs.getInt("id"),
                        rs.getInt("prestito_id"),
                        rs.getDouble("importo"),
                        rs.getInt("pagata") == 1));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero delle sanzioni: " + e.getMessage(), e);
        }
        return sanzioni;
    }

    public void updatePagata(int idPrestito, boolean pagata) {
        String sql = "UPDATE Sanzione SET pagata = ? WHERE prestito_id = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pagata ? 1 : 0);
            pstmt.setInt(2, idPrestito);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Errore DAO durante l'aggiornamento del pagamento della Sanzione: " + e.getMessage(), e);
        }
    }
}
