package sk.oblecma;

import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slavomír
 */
public class Oblecenie {

    private String nazov;

    private Long vlastnikID;
    private Long idOblecenia;

    private Long kategoria;

    private boolean nepremokave;
    private boolean neprefuka;
    private boolean zateplene;

    private boolean formalne;
    private boolean nove;
    private boolean nosene;
    private boolean stare;

    private int pocetObleceniBezPrania;
    private boolean vPrani;

    private boolean mozeSaPoziciavat;

    private Long idObrazka;
    
    public Oblecenie(String nazov, Long kategoria) {
        this.nazov = nazov;
        this.kategoria = kategoria;
        this.nove = true;
        this.pocetObleceniBezPrania = 0;
        this.mozeSaPoziciavat = true;
        this.idObrazka=0L;
    }

   
   
    public Oblecenie() {
    }

    
    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public Long getVlastnikID() {
        return vlastnikID;
    }

    public Long getIdOblecenia() {
        return idOblecenia;
    }

    public boolean isNove() {
        return nove;
    }

    public boolean isNosene() {
        return nosene;
    }

    public boolean isStare() {
        return stare;
    }

    public boolean isMozeSaPoziciavat() {
        return mozeSaPoziciavat;
    }

    public Long getIdObrazka() {
        return idObrazka;
    }

    public void setVlastnikID(Long vlastnikID) {
        this.vlastnikID = vlastnikID;
    }

    public void setIdOblecenia(Long idOblecenia) {
        this.idOblecenia = idOblecenia;
    }

    public void setNepremokave(boolean nepremokave) {
        this.nepremokave = nepremokave;
    }

    public void setNeprefuka(boolean neprefuka) {
        this.neprefuka = neprefuka;
    }

    public void setZateplene(boolean zateplene) {
        this.zateplene = zateplene;
    }

    public void setFormalne(boolean formalne) {
        this.formalne = formalne;
    }

    public void setNove(boolean nove) {
        this.nove = nove;
    }

    public void setNosene(boolean nosene) {
        this.nosene = nosene;
    }

    public void setStare(boolean stare) {
        this.stare = stare;
    }

    public void setPocetObleceniBezPrania(int pocetObleceniBezPrania) {
        this.pocetObleceniBezPrania = pocetObleceniBezPrania;
    }

    public void setvPrani(boolean vPrani) {
        this.vPrani = vPrani;
    }

    public void setMozeSaPoziciavat(boolean mozeSaPoziciavat) {
        this.mozeSaPoziciavat = mozeSaPoziciavat;
    }

    /*private Color farba;
     */
    public void setIdObrazka(Long idObrazka) {
        this.idObrazka = idObrazka;
    }

    public void use(){
        if(vPrani){
            return;
        }
        pocetObleceniBezPrania++;
        if(nove)
        {
            nove=false;
            nosene=true;
        }
    }

    public int getPocetObleceniBezPrania() {
        return pocetObleceniBezPrania;
    }
    
    public boolean isvPrani() {
        return vPrani;
    }
    

    public String getNazov() {
        return nazov;
    }

    void setVlastnikID(int ID) {
        String s = "" + ID;
        this.vlastnikID = Long.parseLong(s);
    }

   
    Long getKategoria() {
        return kategoria;
    }

    public void setIdOblecenia(int idOblecenia) {
        String s = "" + idOblecenia;
        this.idOblecenia = Long.parseLong(s);
    }

    @Override
    public String toString() {
        return nazov;
    }

    void setNepremokave() {
        nepremokave = true;
    }

    void setNeprefuka() {
        neprefuka = true;
    }

    void setFormalne() {
        formalne = true;
    }

    public void setKategoria(Long cisloKategorie) {
        kategoria = cisloKategorie;
    }

    public boolean isNepremokave() {
        return nepremokave;
    }

    public boolean isNeprefuka() {
        return neprefuka;
    }

    public boolean isZateplene() {
        return zateplene;
    }

    public boolean isFormalne() {
        return formalne;
    }

    void setZateplene() {
        zateplene = true;
    }
  
}
