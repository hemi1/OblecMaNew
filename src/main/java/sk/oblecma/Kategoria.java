package sk.oblecma;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slavomír
 */
public class Kategoria {

    public static final int HLAVA = 1;
    public static final int KRK = 2;
    public static final int TELO = 3;
    public static final int NOHY = 4;
    public static final int CHODIDLA = 5;
    public static final int RUKY = 6;

    public static final Long ineFinal = -4L;

    private String nazov;
    private Long cislo;
    private int lokacia;
    private int vrstva;

    private Long pouzivatelID;

    private int maximalnyPocetNoseniBezPrania;

    public Long getPouzivatelID() {
        return pouzivatelID;
    }

    public void setPouzivatelID(Long pouzivatelID) {
        this.pouzivatelID = pouzivatelID;
    }

    public Kategoria() {
    }

    public Kategoria(Long cislo, String nazov, int lokacia, int vrstva, Long vlastnikID, int maximalnyPocetPouziti) {
        this.nazov = nazov;
        this.cislo = cislo;
        this.lokacia = lokacia;
        this.vrstva = vrstva;
        this.pouzivatelID = vlastnikID;
        this.maximalnyPocetNoseniBezPrania = maximalnyPocetPouziti;
    }

    public int getMaximalnyPocetNoseniBezPrania() {
        return maximalnyPocetNoseniBezPrania;
    }

    public void setMaximalnyPocetNoseniBezPrania(int maximalnyPocetNoseniBezPrania) {
        this.maximalnyPocetNoseniBezPrania = maximalnyPocetNoseniBezPrania;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public void setLokacia(int lokacia) {
        this.lokacia = lokacia;
    }

    public void setVrstva(int vrstva) {
        this.vrstva = vrstva;
    }

    /*
    public int getCislo() {
        return cislo.intValue();
    }
     */
    public void setCislo(Long cislo) {
        this.cislo = cislo;
    }

    public Long getCislo() {
        return cislo;
    }

    /**
     * @return the lokacia
     */
    public int getLokacia() {
        return lokacia;
    }

    /**
     * @return the vrstva
     */
    public int getVrstva() {
        return vrstva;
    }
}
