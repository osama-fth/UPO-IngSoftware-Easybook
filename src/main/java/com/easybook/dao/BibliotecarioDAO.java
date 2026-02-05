package com.easybook.dao;

import com.easybook.model.Bibliotecario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class BibliotecarioDAO {

    public Bibliotecario login(String username, String password) {
        String sql = "SELECT * FROM Bibliotecario WHERE username = ? AND password = ?";

        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            Bibliotecario bibliotecario = null;
            if (rs.next()) {
                bibliotecario = new Bibliotecario(
                        rs.getString("matricola"),
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
            rs.close();
            pstmt.close();
            return bibliotecario;

        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il login", e);
        }
    }
}
