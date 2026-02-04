package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Sanzione.
 *
 * @author Negrini Riccardo 20054675
 */

public class Sanzione {
    private int idprestito;
    private double importo;
    private int pagata;

    public Sanzione(int idprestito, double importo){
        this.idprestito=idprestito;
        this.importo=importo;
        this.pagata=0;
    }
    public Sanzione(int idprestito, double importo, int pagata){
        this.idprestito=idprestito;
        this.importo=importo;
        this.pagata=pagata;
    }


    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public int getPagata() {
        return pagata;
    }

    public void setPagata(int pagata) {
        this.pagata = pagata;
    }

    public int getIdprestito() {
        return idprestito;
    }

    public void setIdprestito(int idprestito) {
        this.idprestito = idprestito;
    }
}

