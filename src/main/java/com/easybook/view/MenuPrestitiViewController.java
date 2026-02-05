package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.CatalogoController;
import com.easybook.controller.PrestitoController;
import com.easybook.controller.RestituzioneController;
import com.easybook.controller.UtenteController;
import com.easybook.model.Libro;
import com.easybook.model.Prestito;
import com.easybook.model.Utente;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller grafico per la schermata Gestione Prestiti.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */
public class MenuPrestitiViewController {

    @FXML
    private TableView<Prestito> tabellaPrestiti;
    @FXML
    private TableColumn<Prestito, Integer> colId;
    @FXML
    private TableColumn<Prestito, String> colUtente;
    @FXML
    private TableColumn<Prestito, String> colLibro;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataScadenza;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataRestituzione;
    @FXML
    private TableColumn<Prestito, String> colStato;

    @FXML
    private ComboBox<String> cmbCfUtente;
    @FXML
    private ComboBox<String> cmbIsbnLibro;
    @FXML
    private DatePicker datePrestito;
    @FXML
    private Label lblMessaggio;

    private PrestitoController prestitoController;
    private RestituzioneController restituzioneController;
    private UtenteController utenteController;
    private CatalogoController catalogoController;
    private ObservableList<Prestito> listaPrestiti;

    @FXML
    public void initialize() {
        prestitoController = new PrestitoController();
        restituzioneController = new RestituzioneController();
        utenteController = new UtenteController();
        catalogoController = new CatalogoController();
        listaPrestiti = FXCollections.observableArrayList();

        // Configurazione colonne
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        colUtente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUtente().getCf()));

        colLibro.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibro().getTitolo()));

        colDataInizio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataInizio()));

        colDataScadenza
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataScadenza()));

        colDataRestituzione
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataRestituzione()));

        colStato.setCellValueFactory(cellData -> {
            Prestito p = cellData.getValue();
            String stato;
            if (p.getDataRestituzione() != null) {
                stato = "Restituito";
            } else if (LocalDate.now().isAfter(p.getDataScadenza())) {
                stato = "In ritardo";
            } else {
                stato = "Attivo";
            }
            return new SimpleStringProperty(stato);
        });

        tabellaPrestiti.setItems(listaPrestiti);

        // Popola le ComboBox
        caricaUtentiELibri();

        // Inizializza DatePicker con data odierna
        datePrestito.setValue(LocalDate.now());

        aggiornaTabella();
    }

    @FXML
    private void handleRegistraPrestito() {
        try {
            pulisciMessaggio();

            String cf = cmbCfUtente.getValue();
            String isbn = cmbIsbnLibro.getValue();
            LocalDate dataInizio = datePrestito.getValue();

            if (cf == null || isbn == null || cf.isEmpty() || isbn.isEmpty()) {
                mostraErrore("Seleziona CF Utente e ISBN Libro.");
                return;
            }

            if (dataInizio == null) {
                mostraErrore("Seleziona una data per il prestito.");
                return;
            }

            // Estrai solo il CF (prima parte prima del trattino)
            cf = cf.split(" - ")[0].trim();
            // Estrai solo l'ISBN (prima parte prima del trattino)
            isbn = isbn.split(" - ")[0].trim();

            prestitoController.registraPrestito(cf, isbn, dataInizio);

            mostraSuccesso("Prestito registrato con successo!");
            svuotaCampi();
            aggiornaTabella();

        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore imprevisto: " + e.getMessage());
        }
    }

    @FXML
    private void handleRestituzione() {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Seleziona un prestito dalla tabella.");
            return;
        }

        if (selezionato.getDataRestituzione() != null) {
            mostraErrore("Prestito già restituito.");
            return;
        }

        try {
            pulisciMessaggio();
            restituzioneController.registraRestituzione(selezionato.getId());

            // Verifica se è stata applicata una sanzione
            if (restituzioneController.hasSanzione(selezionato.getId())) {
                mostraErrore("Restituzione registrata. ATTENZIONE: Applicata sanzione per ritardo!");
            } else {
                mostraSuccesso("Restituzione registrata con successo!");
            }

            tabellaPrestiti.getSelectionModel().clearSelection();
            aggiornaTabella();
        } catch (IllegalArgumentException e) {
            mostraErrore(e.getMessage());
        }
    }

    @FXML
    private void vaiASanzioni() throws IOException {
        App.setRoot("sanzioni");
    }

    @FXML
    private void tornaHome() throws IOException {
        App.setRoot("home");
    }

    private void aggiornaTabella() {
        listaPrestiti.clear();
        listaPrestiti.addAll(prestitoController.getTuttiIPrestiti());
        caricaUtentiELibri(); // Aggiorna anche le ComboBox
    }

    private void svuotaCampi() {
        cmbCfUtente.setValue(null);
        cmbIsbnLibro.setValue(null);
        datePrestito.setValue(LocalDate.now());
        tabellaPrestiti.getSelectionModel().clearSelection();
    }

    private void caricaUtentiELibri() {
        // Popola ComboBox Utenti
        ObservableList<String> listaUtenti = FXCollections.observableArrayList();
        for (Utente u : utenteController.getElencoUtenti()) {
            // Formato: CF - Nome Cognome (Stato)
            listaUtenti.add(u.getCf() + " - " + u.getNome() + " " + u.getCognome());
        }
        cmbCfUtente.setItems(listaUtenti);

        // Popola ComboBox Libri
        ObservableList<String> listaLibri = FXCollections.observableArrayList();
        for (Libro l : catalogoController.getTuttiILibri()) {
            // Formato: ISBN - Titolo (Autore) [Copie disp./tot.]
            listaLibri.add(l.getIsbn() + " - " + l.getTitolo() + " (" + l.getAutore() + ")");
        }
        cmbIsbnLibro.setItems(listaLibri);
    }

    private void pulisciMessaggio() {
        lblMessaggio.setText("");
        lblMessaggio.setStyle("-fx-text-fill: black;");
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
