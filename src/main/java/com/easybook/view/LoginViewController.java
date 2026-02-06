package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.BibliotecarioController;
import com.easybook.model.Bibliotecario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller grafico per la schermata Login.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class LoginViewController {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblErrore;

    private BibliotecarioController bibliotecarioController;

    @FXML
    public void initialize() {
        bibliotecarioController = new BibliotecarioController();
    }

    @FXML
    private void handleLogin() {

        lblErrore.setText("");

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {

            Bibliotecario bibliotecario = bibliotecarioController.login(username, password);

            if (bibliotecario != null) {
                App.setRoot("home");
            } else {
                lblErrore.setText("Username o password errati!");
            }

        } catch (IllegalArgumentException e) {
            lblErrore.setText(e.getMessage());
        } catch (IOException e) {
            lblErrore.setText("Errore caricamento Home: " + e.getMessage());
        } catch (Exception e) {
            lblErrore.setText("Errore durante il login: " + e.getMessage());
        }
    }
}
