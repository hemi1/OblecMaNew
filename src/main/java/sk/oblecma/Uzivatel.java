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
public class Uzivatel {
    private Long id;
    private String meno;
    private String heslo;
    private boolean muz;
    
    public static final Long globalUser = -1L;
    
    public Uzivatel()
    {
    }

    public Uzivatel(String meno, String heslo,boolean muz) {
        this.meno=meno;
        this.heslo=heslo;
        this.muz=muz;
    }

    public String getMeno() {
        return meno;
    }

    public String getHeslo() {
        return heslo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public void setMuz(boolean muz) {
        this.muz = muz;
    }
    
    public boolean zena()
    {
        return !muz;
    }
    public boolean muz()
    {
        return muz;
    }
    
}
