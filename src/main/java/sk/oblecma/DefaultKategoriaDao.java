package sk.oblecma;

import java.util.List;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public class DefaultKategoriaDao implements KategoriaDao {

    KategoriaDao kategoria = KategoriaDaoFactory.instance.dajKategoriaDao();
    
    @Override
    public String vratNazov(Long cislo,Long pouzivatelID) {
      return  kategoria.vratNazov(cislo,pouzivatelID);
    }

    @Override
    public Long vratCislo(String nazov,Long pouzivatelID) {
        return kategoria.vratCislo(nazov,pouzivatelID);
    }

    @Override
    public int vratPocet(Long pouzivatelID) {
     return  kategoria.vratPocet(pouzivatelID);
    }

    @Override
    public void pridajKategoriu(Kategoria kategoria,Long pouzivatelID) {
       this.kategoria.pridajKategoriu(kategoria,pouzivatelID);
    }

    @Override
    public List<Kategoria> vratZoznamKategorii(Long pouzivatelID) {
        return kategoria.vratZoznamKategorii(pouzivatelID);
    }

    @Override
    public void odstranKategoriu(String nazovKategorie,Long pouzivatelID) {
       
        kategoria.odstranKategoriu(nazovKategorie,pouzivatelID);
    }

    @Override
    public Kategoria vratKategoriu(Long cislo,Long pouzivatelID) {
        return kategoria.vratKategoriu(cislo,pouzivatelID);
    }

    @Override
    public Kategoria vratKategoriu(String nazovKategorie,Long pouzivatelID) {
        return kategoria.vratKategoriu(nazovKategorie,pouzivatelID);
    }

    @Override
    public void upravKategoriu(Kategoria upravovanaKategoria, Long aktualneID) {
       kategoria.upravKategoriu(upravovanaKategoria, aktualneID);
    }

    
    
}
