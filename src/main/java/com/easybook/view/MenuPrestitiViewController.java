package com.easybook.view;

import com.easybook.App;
import javafx.fxml.FXML;

import java.io.IOException;

/**
 * Controller grafico per la schermata Gestione Prestiti.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class MenuPrestitiViewController {

    @FXML
    private void vaiARegistraPrestito() throws IOException {
        App.setRoot("prestito");
    }

    @FXML
    private void vaiARestituzione() throws IOException {
        App.setRoot("restituzione");
    }

    @FXML
    private void vaiAListaPrestiti() throws IOException {
        App.setRoot("listaprestiti");
    }

    @FXML
    private void tornaHome() throws IOException {
        App.setRoot("home");
    }
}