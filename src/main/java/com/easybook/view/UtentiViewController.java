package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.UtenteController;
import com.easybook.model.StatoUtente;
import com.easybook.model.Utente;
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
 * Controller grafico per la schermata di Gestione Utenti (UC2).
 *
 * @author Foutih Osama 20054809
 */
public class UtentiViewController {

    // Componenti FXML
    @FXML
    private TableView<Utente> tabellaUtenti;
    @FXML
    private TableColumn<Utente, String> colCf;
    @FXML
    private TableColumn<Utente, String> colNome;
    @FXML
    private TableColumn<Utente, String> colCognome;
    @FXML
    private TableColumn<Utente, String> colStato;
    @FXML
    private TableColumn<Utente, Integer> colPrestiti;

    @FXML
    private TextField txtCf;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCognome;

    @FXML
    private Label lblMessaggio;

    // Backend e Dati
    private UtenteController utenteController;
    private ObservableList<Utente> listaUtenti;

    @FXML
    public void initialize() {
        utenteController = new UtenteController();
        listaUtenti = FXCollections.observableArrayList();

        // Configurazione colonne
        colCf.setCellValueFactory(new PropertyValueFactory<>("cf"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato")); // Chiama getStato() -> String
        colPrestiti.setCellValueFactory(new PropertyValueFactory<>("numPrestitiAttivi"));

        tabellaUtenti.setItems(listaUtenti);

        // Listener per popolare i campi quando si seleziona una riga
        tabellaUtenti.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        popolaCampi(newSelection);
                    }
                });

        caricaDati();
    }

    @FXML
    private void handleRegistra() {
        try {
            pulisciMessaggio();
            // Creazione oggetto temporaneo per passare i dati al controller
            // Stato di default ATTIVO, prestiti 0
            Utente nuovo = new Utente(
                    txtCf.getText(),
                    txtNome.getText(),
                    txtCognome.getText(),
                    StatoUtente.ATTIVO,
                    0);

            utenteController.registraUtente(nuovo);

            mostraSuccesso("Utente registrato con successo!");
            svuotaCampi();
            caricaDati();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore imprevisto: " + e.getMessage());
        }
    }

    @FXML
    private void handleModifica() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente dalla tabella.");
            return;
        }

        try {
            pulisciMessaggio();

            Utente modificato = new Utente(
                    selezionato.getCf(),
                    txtNome.getText(),
                    txtCognome.getText(),
                    StatoUtente.valueOf(selezionato.getStato()),
                    selezionato.getNumPrestitiAttivi());

            utenteController.modificaUtente(modificato);

            mostraSuccesso("Dati utente aggiornati.");
            svuotaCampi();
            caricaDati();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void handleRimuovi() {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente da rimuovere.");
            return;
        }

        try {
            pulisciMessaggio();
            utenteController.rimuoviUtente(selezionato.getCf());

            mostraSuccesso("Utente rimosso.");
            svuotaCampi();
            caricaDati();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void handleSospendi() {
        cambiaStato(StatoUtente.SOSPESO);
    }

    @FXML
    private void handleRiattiva() {
        cambiaStato(StatoUtente.ATTIVO);
    }

    private void cambiaStato(StatoUtente nuovoStato) {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un utente.");
            return;
        }

        try {
            pulisciMessaggio();
            if (nuovoStato == StatoUtente.SOSPESO) {
                utenteController.sospendiUtente(selezionato.getCf());
                mostraSuccesso("Utente SOSPESO.");
            } else {
                utenteController.riattivaUtente(selezionato.getCf());
                mostraSuccesso("Utente RIATTIVATO.");
            }
            svuotaCampi();
            caricaDati();
        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void tornaHome() throws IOException {
        App.setRoot("home");
    }

    private void caricaDati() {
        listaUtenti.clear();
        listaUtenti.addAll(utenteController.getElencoUtenti());
    }

    private void popolaCampi(Utente u) {
        txtCf.setText(u.getCf());
        txtNome.setText(u.getNome());
        txtCognome.setText(u.getCognome());
    }

    private void svuotaCampi() {
        txtCf.clear();
        txtNome.clear();
        txtCognome.clear();
        tabellaUtenti.getSelectionModel().clearSelection();
    }

    private void mostraErrore(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: red;");
    }

    private void mostraSuccesso(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setStyle("-fx-text-fill: green;");
    }

    private void pulisciMessaggio() {
        lblMessaggio.setText("");
    }
}
