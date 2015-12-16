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
public interface KategoriaDao {
    public String vratNazov(Long cislo,Long pouzivatelID);
    public Long vratCislo(String nazov,Long pouzivatelID);
    public int vratPocet(Long pouzivatelID);
    public void pridajKategoriu(Kategoria kategoria,Long pouzivatelID);
    public List<Kategoria> vratZoznamKategorii(Long pouzivatelID); 
    public void odstranKategoriu(String nazovKategorie,Long pouzivatelID);
    public Kategoria vratKategoriu(Long cislo,Long pouzivatelID);
    public Kategoria vratKategoriu(String nazovKategorie,Long pouzivatelID);

    public void upravKategoriu(Kategoria upravovanaKategoria, Long aktualneID);
    
}
