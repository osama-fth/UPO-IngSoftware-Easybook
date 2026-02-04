package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.PrestitoController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller grafico per la Registrazione Prestito.
 *
 * @author Lorenzo Bellotti 20054630
 */
public class PrestitoViewController {

    @FXML
    private TextField txtCfUtente;
    @FXML
    private TextField txtIsbnLibro;
    @FXML
    private Label lblMessaggio;

    private PrestitoController prestitoController;

    @FXML
    public void initialize() {
        this.prestitoController = new PrestitoController();
    }

    @FXML
    private void handleRegistraPrestito() {
        String cf = txtCfUtente.getText().trim();
        String isbn = txtIsbnLibro.getText().trim();

        lblMessaggio.setText("");

        if (cf.isEmpty() || isbn.isEmpty()) {
            mostraErrore("Per favore, inserisci sia il CF Utente che l'ISBN.");
            return;
        }

        try {
            prestitoController.registraPrestito(cf, isbn);

            mostraSuccesso("Prestito registrato con successo!");
            svuotaCampi();

        } catch (IllegalArgumentException e) {
            mostraErrore("Errore: " + e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore di sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void tornaGestione() throws IOException {
        App.setRoot("menuprestiti");
    }

    private void svuotaCampi() {
        txtCfUtente.clear();
        txtIsbnLibro.clear();
    }

    private void mostraErrore(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: red;");
    }

    private void mostraSuccesso(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: green;");
    }
}