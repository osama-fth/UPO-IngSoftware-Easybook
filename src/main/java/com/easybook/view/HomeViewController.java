package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.BibliotecarioController;
import com.easybook.model.Bibliotecario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Controller grafico per la schermata Home.
 *
 * @author Foutih Osama 20054809
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class HomeViewController {

    @FXML
    private Label lblBenvenuto;

    @FXML
    public void initialize() {
        Bibliotecario bibliotecario = BibliotecarioController.getBibliotecarioCorrente();

        if (bibliotecario != null) {
            if (lblBenvenuto != null) {
                lblBenvenuto.setText("Benvenuto " + bibliotecario.getNome() + " " + bibliotecario.getCognome());
            }
        } else {
            try {
                App.setRoot("login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void vaiAlCatalogo() throws IOException {
        if (BibliotecarioController.isAutenticato()) {
            App.setRoot("catalogo");
        } else {
            App.setRoot("login");
        }
    }

    @FXML
    private void vaiAgliUtenti() throws IOException {
        if (BibliotecarioController.isAutenticato()) {
            App.setRoot("utenti");
        } else {
            App.setRoot("login");
        }
    }

    @FXML
    private void vaiAMenuPrestiti() throws IOException {
        if (BibliotecarioController.isAutenticato()) {
            App.setRoot("menuprestiti");
        } else {
            App.setRoot("login");
        }
    }

    @FXML
    private void logout() throws IOException {
        BibliotecarioController controller = new BibliotecarioController();
        controller.logout();

        App.setRoot("login");
    }
}
