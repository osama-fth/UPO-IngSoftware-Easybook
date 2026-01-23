package com.easybook.dao;

import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Foutih Osama 20054809
 */
public class UtenteDAO {

    public void insert(Utente utente) {
        String sql = "INSERT INTO Utente(cf, nome, cognome, stato, num_prestiti_attivi) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utente.getCf());
            pstmt.setString(2, utente.getNome());
            pstmt.setString(3, utente.getCognome());
            pstmt.setString(4, utente.getStato());
            pstmt.setInt(5, utente.getNumPrestitiAttivi());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento dell'utente con CF " + utente.getCf(), e);
        }
    }

    public Utente findByCf(String cf) {
        String sql = "SELECT * FROM Utente WHERE cf = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cf);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String statoString = rs.getString("stato");
                StatoUtente statoEnum;
                try {
                    statoEnum = StatoUtente.valueOf(statoString);
                } catch (IllegalArgumentException | NullPointerException e) {
                    statoEnum = StatoUtente.ATTIVO;
                }

                return new Utente(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        statoEnum,
                        rs.getInt("num_prestiti_attivi"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca dell'utente con CF: " + cf, e);
        }
        return null;
    }

    /**
     * Recupera tutti gli utenti (UC2 - Lista utenti).
     */
    public List<Utente> findAll() {
        List<Utente> utenti = new ArrayList<>();
        String sql = "SELECT * FROM Utente";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String statoString = rs.getString("stato");
                StatoUtente statoEnum;
                try {
                    statoEnum = StatoUtente.valueOf(statoString);
                } catch (IllegalArgumentException | NullPointerException e) {
                    statoEnum = StatoUtente.ATTIVO;
                }

                Utente u = new Utente(
                        rs.getString("cf"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        statoEnum,
                        rs.getInt("num_prestiti_attivi"));
                utenti.add(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero della lista utenti", e);
        }
        return utenti;
    }

    /**
     * Aggiorna i dati anagrafici di un utente (UC2 - Modifica utente).
     */
    public void update(Utente utente) {
        String sql = "UPDATE Utente SET nome = ?, cognome = ?, stato = ?, num_prestiti_attivi = ? WHERE cf = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utente.getNome());
            pstmt.setString(2, utente.getCognome());
            pstmt.setString(3, utente.getStato());
            pstmt.setInt(4, utente.getNumPrestitiAttivi());
            pstmt.setString(5, utente.getCf());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun utente trovato con CF: " + utente.getCf());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento dell'utente: " + utente.getCf(), e);
        }
    }

    public void updateStato(String cf, StatoUtente nuovoStato) {
        String sql = "UPDATE Utente SET stato = ? WHERE cf = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuovoStato.toString());
            pstmt.setString(2, cf);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento dello stato per l'utente: " + cf, e);
        }
    }

    public void updateNumPrestiti(String cf, int nuovoNumero) {
        String sql = "UPDATE Utente SET num_prestiti_attivi = ? WHERE cf = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuovoNumero);
            pstmt.setString(2, cf);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento dei prestiti per l'utente: " + cf, e);
        }
    }

    /**
     * Elimina un utente.
     */
    public void delete(String cf) {
        String sql = "DELETE FROM Utente WHERE cf = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cf);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun utente trovato con CF: " + cf);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'eliminazione dell'utente con CF: " + cf, e);
        }
    }
}
