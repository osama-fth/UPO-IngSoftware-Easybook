package com.easybook.view;

import com.easybook.App;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.SanzioneDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.Prestito;
import com.easybook.model.Sanzione;
import com.easybook.model.StatoUtente;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;

/**
 * Controller grafico per la schermata Gestione Sanzioni.
 *
 * @author Foutih Osama 20054809
 * @author Riccardo Negrini 20054675
 */
public class SanzioniViewController {

    @FXML
    private TableView<Sanzione> tabellaSanzioni;
    @FXML
    private TableColumn<Sanzione, Integer> colId;
    @FXML
    private TableColumn<Sanzione, Integer> colIdPrestito;
    @FXML
    private TableColumn<Sanzione, String> colUtente;
    @FXML
    private TableColumn<Sanzione, String> colLibro;
    @FXML
    private TableColumn<Sanzione, Double> colImporto;
    @FXML
    private TableColumn<Sanzione, String> colStato;

    @FXML
    private Label lblMessaggio;

    private SanzioneDAO sanzioneDAO;
    private PrestitoDAO prestitoDAO;
    private UtenteDAO utenteDAO;
    private ObservableList<Sanzione> listaSanzioni;

    @FXML
    public void initialize() {
        sanzioneDAO = new SanzioneDAO();
        prestitoDAO = new PrestitoDAO();
        utenteDAO = new UtenteDAO();
        listaSanzioni = FXCollections.observableArrayList();

        // Configurazione colonne
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        colIdPrestito.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getIdprestito()).asObject());

        colUtente.setCellValueFactory(cellData -> {
            Sanzione s = cellData.getValue();
            Prestito p = prestitoDAO.findById(s.getIdprestito());
            if (p != null && p.getUtente() != null) {
                return new SimpleStringProperty(p.getUtente().getCf());
            }
            return new SimpleStringProperty("N/D");
        });

        colLibro.setCellValueFactory(cellData -> {
            Sanzione s = cellData.getValue();
            Prestito p = prestitoDAO.findById(s.getIdprestito());
            if (p != null && p.getLibro() != null) {
                return new SimpleStringProperty(p.getLibro().getTitolo());
            }
            return new SimpleStringProperty("N/D");
        });

        colImporto
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getImporto()).asObject());

        colStato.setCellValueFactory(cellData -> {
            boolean pagata = cellData.getValue().isPagata();
            return new SimpleStringProperty(pagata ? "PAGATA" : "DA PAGARE");
        });

        tabellaSanzioni.setItems(listaSanzioni);
        aggiornaTabella();
    }

    @FXML
    private void handlePagata() {
        Sanzione selezionata = tabellaSanzioni.getSelectionModel().getSelectedItem();
        if (selezionata == null) {
            mostraErrore("Seleziona una sanzione dalla tabella.");
            return;
        }

        if (selezionata.isPagata()) {
            mostraErrore("La sanzione è già stata pagata.");
            return;
        }

        try {
            pulisciMessaggio();

            // Aggiorna lo stato della sanzione
            sanzioneDAO.updatePagata(selezionata.getIdprestito(), true);

            // Riattiva l'utente associato
            Prestito prestito = prestitoDAO.findById(selezionata.getIdprestito());
            if (prestito != null && prestito.getUtente() != null) {
                utenteDAO.updateStato(prestito.getUtente().getCf(), StatoUtente.ATTIVO);
            }

            mostraSuccesso("Sanzione segnata come pagata. Utente riattivato.");
            tabellaSanzioni.getSelectionModel().clearSelection();
            aggiornaTabella();

        } catch (Exception e) {
            mostraErrore("Errore: " + e.getMessage());
        }
    }

    @FXML
    private void aggiornaTabella() {
        listaSanzioni.clear();
        listaSanzioni.addAll(sanzioneDAO.findAll());
    }

    @FXML
    private void tornaIndietro() throws IOException {
        App.setRoot("menuprestiti");
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
