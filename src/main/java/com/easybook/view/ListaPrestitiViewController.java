package com.easybook.view;

import com.easybook.App;
import com.easybook.controller.PrestitoController;
import com.easybook.model.Prestito;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Controller grafico per la schermata Lista Prestiti.
 *
 * @author Lorenzo Bellotti 20054630
 * @author Riccardo Negrini 20054675
 */

public class ListaPrestitiViewController {

    @FXML
    private TableView<Prestito> tabellaPrestiti;

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

    private PrestitoController prestitoController;
    private ObservableList<Prestito> listaDati;

    @FXML
    public void initialize() {
        prestitoController = new PrestitoController();
        listaDati = FXCollections.observableArrayList();

        // Configurazione Colonne
        // 1. Per l'Utente mostriamo "Cognome Nome" accedendo all'oggetto Utente dentro Prestito
        colUtente.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getUtente().getCognome() + " " +
                                cellData.getValue().getUtente().getNome()
                ));

        // 2. Per il Libro mostriamo il Titolo
        colLibro.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLibro().getTitolo()));

        // 3. Date (Assumendo che nel model Prestito i getter siano getDataInizio(), etc.)
        colDataInizio.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDataInizio()));

        colDataScadenza.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDataScadenza()));

        // La data restituzione potrebbe essere null (prestito ancora aperto)
        colDataRestituzione.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDataRestituzione()));

        // Caricamento dati
        aggiornaTabella();
    }

    private void aggiornaTabella() {
        listaDati.clear();
        listaDati.addAll(prestitoController.getTuttiIPrestiti());
        tabellaPrestiti.setItems(listaDati);
    }

    @FXML
    private void tornaIndietro() throws IOException {
        // Torna al menu intermedio "Gestione Prestiti"
        App.setRoot("menuprestiti");
    }
}