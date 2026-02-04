package com.easybook.model;

/**
 * Classe POJO che rappresenta l'entità Sanzione.
 *
 * @author Negrini Riccardo 20054675
 */

public class Sanzione {
    private int idprestito;
    private float importo;
    private int pagata;

    public Sanzione(int idprestito, float importo){
        this.idprestito=idprestito;
        this.importo=importo;
        this.pagata=0;
    }

    public float getImporto() {
        return importo;
    }

    public void setImporto(float importo) {
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

