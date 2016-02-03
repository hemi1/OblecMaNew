/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

/**
 *
 * @author Rastislav
 */
public class Obuv {
    private String nazov;
    private Long vlastnikID;
    private Long idObuvy;

    public Obuv() {
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public Long getVlastnikID() {
        return vlastnikID;
    }

    public void setVlastnikID(Long vlastnikID) {
        this.vlastnikID = vlastnikID;
    }

    public Long getIdObuvy() {
        return idObuvy;
    }

    public void setIdObuvy(Long idObuvy) {
        this.idObuvy = idObuvy;
    }
     public void setIdObuvy(int idObuvy) {
        String s = "" + idObuvy;
        this.idObuvy = Long.parseLong(s);
    } 
    public boolean isZateplene() {
        return zateplene;
    }

    public void setZateplene(boolean zateplene) {
        this.zateplene = zateplene;
    }

    public boolean isNepremokave() {
        return nepremokave;
    }

    public void setNepremokave(boolean nepremokave) {
        this.nepremokave = nepremokave;
    }

    public boolean isVetrane() {
        return vetrane;
    }

    public void setVetrane(boolean vetrane) {
        this.vetrane = vetrane;
    }
    private boolean zateplene;
    private boolean nepremokave;
    private boolean vetrane;

  
}
