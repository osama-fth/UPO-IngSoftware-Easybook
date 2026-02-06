package com.easybook.controller;

import com.easybook.dao.LibroDAO;
import com.easybook.dao.PrestitoDAO;
import com.easybook.dao.SanzioneDAO;
import com.easybook.dao.UtenteDAO;
import com.easybook.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

        prestitoDAO.update(prestito);

        Libro libro = libroDAO.findByIsbn(prestito.getLibro().getIsbn());
        libroDAO.updateCopie(libro.getIsbn(), libro.getCopieDisponibili() + 1);

        Utente utente = utenteDAO.findByCf(prestito.getUtente().getCf());
        utente.setNumPrestitiAttivi(Math.max(0, utente.getNumPrestitiAttivi() - 1));
        utenteDAO.update(utente);

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

        Sanzione sanzione = new Sanzione(prestito.getId(), importo);
        sanzioneDAO.insert(sanzione);

        utenteDAO.updateStato(prestito.getUtente().getCf(), StatoUtente.SOSPESO);
    }

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

    public boolean hasSanzione(int idPrestito) {
        return sanzioneDAO.findByPrestitoId(idPrestito) != null;
    }

    public Sanzione getSanzione(int idPrestito) {
        return sanzioneDAO.findByPrestitoId(idPrestito);
    }
}
