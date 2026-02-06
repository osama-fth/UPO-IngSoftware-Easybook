package com.easybook.dao;

import com.easybook.model.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Foutih Osama 20054809
 */
public class LibroDAO {

    public void insert(Libro libro) {
        String sql = "INSERT INTO Libro (isbn, titolo, autore, copie_totali, copie_disponibili) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitolo());
            pstmt.setString(3, libro.getAutore());
            pstmt.setInt(4, libro.getCopieTotali());
            pstmt.setInt(5, libro.getCopieDisponibili());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento del libro: " + e.getMessage(), e);
        }
    }

    public Libro findByIsbn(String isbn) {
        String sql = "SELECT * FROM Libro WHERE isbn = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            ResultSet rs = pstmt.executeQuery();
            Libro libro = null;
            if (rs.next()) {
                libro = new Libro(
                        rs.getString("isbn"),
                        rs.getString("titolo"),
                        rs.getString("autore"),
                        rs.getInt("copie_totali"),
                        rs.getInt("copie_disponibili"));
            }
            rs.close();
            pstmt.close();
            return libro;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca del libro con ISBN: " + isbn, e);
        }
    }

    public List<Libro> findAll() {
        List<Libro> libri = new ArrayList<>();
        String sql = "SELECT * FROM Libro";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                libri.add(new Libro(
                        rs.getString("isbn"),
                        rs.getString("titolo"),
                        rs.getString("autore"),
                        rs.getInt("copie_totali"),
                        rs.getInt("copie_disponibili")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante il recupero dei libri: " + e.getMessage(), e);
        }
        return libri;
    }

    public void update(Libro libro) {
        String sql = "UPDATE Libro SET titolo = ?, autore = ?, copie_totali = ?, copie_disponibili = ? WHERE isbn = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, libro.getTitolo());
            pstmt.setString(2, libro.getAutore());
            pstmt.setInt(3, libro.getCopieTotali());
            pstmt.setInt(4, libro.getCopieDisponibili());
            pstmt.setString(5, libro.getIsbn());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento del libro: " + e.getMessage(), e);
        }
    }

    public void updateCopie(String isbn, int copieDisponibili) {
        String sql = "UPDATE Libro SET copie_disponibili = ? WHERE isbn = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, copieDisponibili);
            pstmt.setString(2, isbn);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento copie: " + e.getMessage(), e);
        }
    }

    public void delete(String isbn) {
        String sql = "DELETE FROM Libro WHERE isbn = ?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'eliminazione del libro: " + e.getMessage(), e);
        }
    }
}
