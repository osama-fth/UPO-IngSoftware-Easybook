package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.SanzioneDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Controller per la gestione della restituzione (UC5) e sanzioni (UC6).
 *
 * @author Riccardo Negrini 20054675
 */
public class RestituzioneController {

    private static final double SANZIONE_BASE = 10.0;
    private static final int GIORNI_SOGLIA = 7;
    private static final double INCREMENTO_GIORNALIERO = 0.50;

    private final SanzioneDAO sanzioneDAO;
    private final PrestitoDAO prestitoDAO;
    private final LibroDAO libroDAO;
    private final UtenteDAO utenteDAO;

    public RestituzioneController() {
        this.sanzioneDAO = new SanzioneDAO();
        this.prestitoDAO = new PrestitoDAO();
        this.libroDAO = new LibroDAO();
        this.utenteDAO = new UtenteDAO();
    }

    public void registraRestituzione(int idPrestito) {
        Prestito prestito = prestitoDAO.findById(idPrestito);

        if (prestito == null) {
            throw new IllegalArgumentException("Prestito con ID " + idPrestito + " non trovato.");
        }

        if (prestito.getDataRestituzione() != null) {
            throw new IllegalArgumentException("Prestito con ID " + idPrestito + " già restituito in data "
                    + prestito.getDataRestituzione());
        }

        LocalDate oggi = LocalDate.now();
        prestito.setDataRestituzione(oggi);

        // Aggiorna il prestito nel database
        prestitoDAO.update(prestito);

        // Incrementa le copie disponibili del libro
        Libro libro = libroDAO.findByIsbn(prestito.getLibro().getIsbn());
        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() + 1);

        // Decrementa il numero di prestiti attivi dell'utente
        Utente utente = utenteDAO.findByCf(prestito.getUtente().getCf());
        utente.setNumPrestitiAttivi(Math.max(0, utente.getNumPrestitiAttivi() - 1));
        utenteDAO.update(utente);

        // Verifica se c'è ritardo e applica eventuale sanzione (UC6)
        if (oggi.isAfter(prestito.getDataScadenza())) {
            applicaSanzione(prestito, oggi);
        }
    }

    /**
     * Applica una sanzione per ritardo nella restituzione (UC6).
     */
    private void applicaSanzione(Prestito prestito, LocalDate dataRestituzione) {
        long giorniRitardo = ChronoUnit.DAYS.between(prestito.getDataScadenza(), dataRestituzione);
        double importo = calcolaImportoSanzione(giorniRitardo);

        // Crea e salva la sanzione
        Sanzione sanzione = new Sanzione(prestito.getId(), importo);
        sanzioneDAO.insert(sanzione);

        // Sospende l'utente (blocca futuri prestiti)
        utenteDAO.updateStato(prestito.getUtente().getCf(), StatoUtente.SOSPESO);
    }

    /**
     * Calcola l'importo della sanzione in base ai giorni di ritardo.
     * - Fino a 7 giorni: 10€ fissi
     * - Dall'8° giorno: 10€ + 0.50€ per ogni giorno oltre il 7°
     */
    public double calcolaImportoSanzione(long giorniRitardo) {
        if (giorniRitardo <= 0) {
            return 0.0;
        }
        if (giorniRitardo <= GIORNI_SOGLIA) {
            return SANZIONE_BASE;
        }
        long giorniExtra = giorniRitardo - GIORNI_SOGLIA;
        return SANZIONE_BASE + (giorniExtra * INCREMENTO_GIORNALIERO);
    }

    /**
     * Restituisce tutti i prestiti attivi (non ancora restituiti).
     */
    public List<Prestito> getPrestitiAttivi() {
        return prestitoDAO.findAll().stream()
                .filter(p -> p.getDataRestituzione() == null)
                .toList();
    }

    /**
     * Verifica se un prestito ha una sanzione associata.
     */
    public boolean hasSanzione(int idPrestito) {
        return sanzioneDAO.findByPrestitoId(idPrestito) != null;
    }

    /**
     * Recupera la sanzione associata a un prestito.
     */
    public Sanzione getSanzione(int idPrestito) {
        return sanzioneDAO.findByPrestitoId(idPrestito);
    }
}
