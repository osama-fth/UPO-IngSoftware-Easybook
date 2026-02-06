package com.easybook.dao;

import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Foutih Osama 20054809
 */
public class UtenteDAO {

    public void insert(Utente utente) {
        String sql = "INSERT INTO Utente (cf, nome, cognome, stato, num_prestiti_attivi) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, utente.getCf());
            pstmt.setString(2, utente.getNome());
            pstmt.setString(3, utente.getCognome());
            pstmt.setString(4, utente.getStato());
            pstmt.setInt(5, utente.getNumPrestitiAttivi());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento dell'utente: " + e.getMessage(), e);
        }
    }

    public Utente findByCf(String cf) {
        String sql = "SELECT * FROM Utente WHERE cf = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cf);
            ResultSet rs = pstmt.executeQuery();
            Utente utente = null;
            if (rs.next()) {
                utente = new Utente(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        StatoUtente.valueOf(rs.getString("stato")),
                        rs.getInt("num_prestiti_attivi"));
            }
            rs.close();
            pstmt.close();
            return utente;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca dell'utente con CF: " + cf, e);
        }
    }

    public List<Utente> findAll() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM Utente";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                utenti.add(new Utente(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        StatoUtente.valueOf(rs.getString("stato")),
                        rs.getInt("num_prestiti_attivi")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero degli utenti: " + e.getMessage(), e);
        }
        return utenti;
    }

    public void update(Utente utente) {
        String sql = "UPDATE Utente SET nome = ?, cognome = ?, stato = ?, num_prestiti_attivi = ? WHERE cf = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, utente.getNome());
            pstmt.setString(2, utente.getCognome());
            pstmt.setString(3, utente.getStato());
            pstmt.setInt(4, utente.getNumPrestitiAttivi());
            pstmt.setString(5, utente.getCf());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento dell'utente: " + e.getMessage(), e);
        }
    }

    public void updateStato(String cf, StatoUtente stato) {
        String sql = "UPDATE Utente SET stato = ? WHERE cf = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stato.name());
            pstmt.setString(2, cf);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento dello stato: " + e.getMessage(), e);
        }
    }

    public void delete(String cf) {
        String sql = "DELETE FROM Utente WHERE cf = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cf);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'eliminazione dell'utente: " + e.getMessage(), e);
        }
    }
}
