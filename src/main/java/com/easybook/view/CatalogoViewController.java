package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.CatalogoController;
import com.easybook.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

/**
 * Controller grafico per la schermata di Gestione Catalogo (UC1).
 *
 * @author Foutih Osama 20054809
 */
public class CatalogoViewController {

    @FXML
    private TableView<Libro> tabellaLibri;
    @FXML
    private TableColumn<Libro, String> colIsbn;
    @FXML
    private TableColumn<Libro, String> colTitolo;
    @FXML
    private TableColumn<Libro, String> colAutore;
    @FXML
    private TableColumn<Libro, Integer> colCopieTotali;
    @FXML
    private TableColumn<Libro, Integer> colCopieDisponibili;

    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitolo;
    @FXML
    private TextField txtAutore;
    @FXML
    private TextField txtCopie;

    @FXML
    private Label lblMessaggio;

    private CatalogoController catalogoController;
    private ObservableList<Libro> listaLibri;

    @FXML
    public void initialize() {
        catalogoController = new CatalogoController();
        listaLibri = FXCollections.observableArrayList();

        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutore.setCellValueFactory(new PropertyValueFactory<>("autore"));
        colCopieTotali.setCellValueFactory(new PropertyValueFactory<>("copieTotali"));
        colCopieDisponibili.setCellValueFactory(new PropertyValueFactory<>("copieDisponibili"));

        tabellaLibri.setItems(listaLibri);

        // Listener per popolare i campi quando si seleziona una riga
        tabellaLibri.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        popolaCampi(newSelection);
                    }
                });

        aggiornaTabella();
    }

    /**
     * Gestisce il click sul bottone "Aggiungi".
     */
    @FXML
    private void handleAggiungi() {
        try {
            lblMessaggio.setText("");
            lblMessaggio.setStyle("-fx-text-fill: black;");

            if (txtIsbn.getText().isEmpty() || txtTitolo.getText().isEmpty() ||
                    txtAutore.getText().isEmpty() || txtCopie.getText().isEmpty()) {
                mostraErrore("Compilare tutti i campi!");
                return;
            }

            int copie;
            try {
                copie = Integer.parseInt(txtCopie.getText());
            } catch (NumberFormatException e) {
                mostraErrore("Il numero copie deve essere un numero intero.");
                return;
            }

            Libro nuovoLibro = new Libro(
                    txtIsbn.getText(),
                    txtTitolo.getText(),
                    txtAutore.getText(),
                    copie,
                    copie);

            catalogoController.aggiungiLibro(nuovoLibro);

            mostraSuccesso("Libro inserito correttamente.");
            svuotaCampi();
            aggiornaTabella();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore imprevisto: " + e.getMessage());
        }
    }

    /**
     * Gestisce il click sul bottone "Modifica Selezionato".
     */
    @FXML
    private void handleModifica() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un libro dalla tabella per modificarlo.");
            return;
        }

        try {
            lblMessaggio.setText("");

            if (txtTitolo.getText().isEmpty() || txtAutore.getText().isEmpty() || txtCopie.getText().isEmpty()) {
                mostraErrore("Compilare tutti i campi!");
                return;
            }

            int copie;
            try {
                copie = Integer.parseInt(txtCopie.getText());
            } catch (NumberFormatException e) {
                mostraErrore("Il numero copie deve essere un numero intero.");
                return;
            }

            // Calcola copie disponibili mantenendo la differenza
            int copiePrestitate = selezionato.getCopieTotali() - selezionato.getCopieDisponibili();
            int nuoveCopieDisponibili = copie - copiePrestitate;

            if (nuoveCopieDisponibili < 0) {
                mostraErrore("Non puoi ridurre le copie totali sotto quelle in prestito (" + copiePrestitate + ").");
                return;
            }

            Libro modificato = new Libro(
                    selezionato.getIsbn(),
                    txtTitolo.getText(),
                    txtAutore.getText(),
                    copie,
                    nuoveCopieDisponibili);

            catalogoController.modificaLibro(modificato);

            mostraSuccesso("Libro modificato correttamente.");
            svuotaCampi();
            aggiornaTabella();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore imprevisto: " + e.getMessage());
        }
    }

    /**
     * Gestisce il click sul bottone "Rimuovi Selezionato".
     */
    @FXML
    private void handleRimuovi() {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();

        if (selezionato == null) {
            mostraErrore("Seleziona un libro dalla tabella per rimuoverlo.");
            return;
        }

        try {
            catalogoController.rimuoviLibro(selezionato.getIsbn());

            mostraSuccesso("Libro rimosso.");
            svuotaCampi();
            aggiornaTabella();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void tornaHome() throws IOException {
        App.setRoot("home");
    }

    private void aggiornaTabella() {
        listaLibri.clear();
        listaLibri.addAll(catalogoController.getTuttiILibri());
    }

    private void popolaCampi(Libro libro) {
        txtIsbn.setText(libro.getIsbn());
        txtTitolo.setText(libro.getTitolo());
        txtAutore.setText(libro.getAutore());
        txtCopie.setText(String.valueOf(libro.getCopieTotali()));
    }

    private void svuotaCampi() {
        txtIsbn.clear();
        txtTitolo.clear();
        txtAutore.clear();
        txtCopie.clear();
        tabellaLibri.getSelectionModel().clearSelection();
    }

    private void mostraErrore(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }

    private void mostraSuccesso(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    }
}
