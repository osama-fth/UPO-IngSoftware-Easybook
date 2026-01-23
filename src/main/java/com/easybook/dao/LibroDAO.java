package com.easybook.dao;

import com.easybook.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Foutih Osama 20054809
 */
public class LibroDAO {

    public void insert(Libro libro) {
        String sql = "INSERT INTO Libro(isbn, titolo, autore, copie_totali, copie_disponibili) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitolo());
            pstmt.setString(3, libro.getAutore());
            pstmt.setInt(4, libro.getCopieTotali());
            pstmt.setInt(5, libro.getCopieDisponibili());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento del libro " + libro.getTitolo(), e);
        }
    }

    public List<Libro> findAll() {
        List<Libro> libri = new ArrayList<>();
        String sql = "SELECT * FROM Libro";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Libro l = new Libro(
                        rs.getString("isbn"),
                        rs.getString("titolo"),
                        rs.getString("autore"),
                        rs.getInt("copie_totali"),
                        rs.getInt("copie_disponibili"));
                libri.add(l);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero della lista libri", e);
        }
        return libri;
    }

    public Libro findByIsbn(String isbn) {
        String sql = "SELECT * FROM Libro WHERE isbn = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Libro(
                        rs.getString("isbn"),
                        rs.getString("titolo"),
                        rs.getString("autore"),
                        rs.getInt("copie_totali"),
                        rs.getInt("copie_disponibili"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca del libro con ISBN: " + isbn, e);
        }
        return null;
    }

    // Metodo per aggiornare la disponibilità (usato nei prestiti)
    public void updateCopie(String isbn, int nuoveCopie) {
        String sql = "UPDATE Libro SET copie_disponibili = ? WHERE isbn = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuoveCopie);
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento copie per ISBN: " + isbn, e);
        }
    }

    /**
     * Aggiorna tutti i campi di un libro.
     */
    public void update(Libro libro) {
        String sql = "UPDATE Libro SET titolo = ?, autore = ?, copie_totali = ?, copie_disponibili = ? WHERE isbn = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libro.getTitolo());
            pstmt.setString(2, libro.getAutore());
            pstmt.setInt(3, libro.getCopieTotali());
            pstmt.setInt(4, libro.getCopieDisponibili());
            pstmt.setString(5, libro.getIsbn());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun libro trovato con ISBN: " + libro.getIsbn());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento del libro con ISBN: " + libro.getIsbn(), e);
        }
    }

    /**
     * Elimina un libro dal catalogo.
     */
    public void delete(String isbn) {
        String sql = "DELETE FROM Libro WHERE isbn = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, isbn);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new RuntimeException("Nessun libro trovato con ISBN: " + isbn);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'eliminazione del libro con ISBN: " + isbn, e);
        }
    }
}
