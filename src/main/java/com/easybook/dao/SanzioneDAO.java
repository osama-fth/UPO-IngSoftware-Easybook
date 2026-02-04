package com.easybook.dao;


import com.easybook.model.Libro;
import com.easybook.model.Sanzione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Riccardo Negrini 20054675
 */

public class SanzioneDAO {

    public void insert(Sanzione sanzione){
        String sql = "INSERT INTO Sanzione (prestito_id,importo) VALUES (?, ?)";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, sanzione.getIdprestito());
            pstmt.setDouble(2,sanzione.getImporto());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'inserimento della Sanzione: " + e.getMessage(), e);
        }
    }

    public Sanzione findbyprestitoid(int idprestito){
        String sql="SELECT * FROM Sanzione WHERE prestito_id = ? ";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idprestito);
            ResultSet rs = pstmt.executeQuery();
            Sanzione sanzione= null;
            if (rs.next()) {
                sanzione= new Sanzione(
                        rs.getInt("prestito_id"),
                        rs.getDouble("importo"),
                        rs.getInt("pagata"));
            }
            rs.close();
            pstmt.close();
            return sanzione;
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante la ricerca della Sanzione con ID_Prestito: " + idprestito, e);
        }
    }

    public void updatePagata(int idprestito,int pagata){
        Sanzione sanzione=findbyprestitoid(idprestito);

        String sql="UPDATE Sanzione SET pagata=? WHERE prestito_id=?";
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pagata);
            assert sanzione != null;
            pstmt.setInt(2, sanzione.getIdprestito());
            ResultSet rs = pstmt.executeQuery();
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Errore DAO durante l'aggiornamento del pagamento della Sanzione con ID_Prestito:" + idprestito, e);
        }

    }
}
