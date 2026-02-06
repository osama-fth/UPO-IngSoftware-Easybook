package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Sanzione.
 *
 * @author Negrini Riccardo 20054675
 */

public class Sanzione {
    private int id;
    private int idprestito;
    private double importo;
    private boolean pagata;

    public Sanzione(int idprestito, double importo) {
        this.idprestito = idprestito;
        this.importo = importo;
        this.pagata = false;
    }

    public Sanzione(int id, int idprestito, double importo, boolean pagata) {
        this.id = id;
        this.idprestito = idprestito;
        this.importo = importo;
        this.pagata = pagata;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public boolean isPagata() {
        return pagata;
    }

    public void setPagata(boolean pagata) {
        this.pagata = pagata;
    }

    public int getIdprestito() {
        return idprestito;
    }

    public void setIdprestito(int idprestito) {
        this.idprestito = idprestito;
    }
}
