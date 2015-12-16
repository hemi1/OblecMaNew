package sk.oblecma;
// hotove testy
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Slavomír
 */
public class PametovyKategoriaDao implements KategoriaDao {
    

    private List<Kategoria> mojaKategoria = new ArrayList<>();

    public PametovyKategoriaDao() {

       /* mojaKategoria.add(new Kategoria(0L, "ponozky", Kategoria.CHODIDLA, 1,Uzivatel.globalUser, 1));
        mojaKategoria.add(new Kategoria(1L, "tricko", Kategoria.TELO, 1,Uzivatel.globalUser, 3));
        mojaKategoria.add(new Kategoria(2L, "topanky", Kategoria.CHODIDLA, 2,Uzivatel.globalUser, Integer.MAX_VALUE));
        mojaKategoria.add(new Kategoria(3L, "nohavice", Kategoria.NOHY, 2,Uzivatel.globalUser, 15));
       */ mojaKategoria.add(new Kategoria(Kategoria.ineFinal, "ine", 0, 0,Uzivatel.globalUser,0));
    }

    @Override
    public String vratNazov(Long cislo,Long pouzivatelID) {
        for (Kategoria kategoria : mojaKategoria) {
            if (kategoria.getCislo().equals( cislo)) {
                return kategoria.getNazov();
            }
        }
        return null;
    }

    @Override
    public Long vratCislo(String nazov,Long pouzivatelID) {
        Long vrat = Kategoria.ineFinal;

        for (Kategoria kategoria : mojaKategoria) {
            if (nazov.equals(kategoria.getNazov())) {
                return kategoria.getCislo();
            }
        }

        return vrat;
    }
    
    @Override
    public int vratPocet(Long pouzivatelID) {
        int pocet = 0;
        
        for(Kategoria kat : mojaKategoria)
        {
            if(kat.getPouzivatelID().equals(pouzivatelID))
            {
                pocet++;
            }
        }
        
        return pocet;
    }

    @Override
    public void pridajKategoriu(Kategoria kategoria,Long pouzivatelID) {
        Long cislo = (long) mojaKategoria.size();
        kategoria.setCislo(cislo);
        kategoria.setPouzivatelID(pouzivatelID);
        mojaKategoria.add(kategoria);
    }

    @Override
    public List<Kategoria> vratZoznamKategorii(Long pouzivatelID) {
        if(mojaKategoria==null)
        {
           return new ArrayList<>();
        }
        
        List<Kategoria> vrat = new ArrayList<>();
        
        for(Kategoria kat : mojaKategoria)
        {
            if(kat.getPouzivatelID().equals(pouzivatelID))
            {
                vrat.add(kat);
            }
        }
        
        return vrat;
    }

    @Override
    public void odstranKategoriu(String nazovKategorie,Long pouzivatelID) {
        Kategoria vymaz = null;
        for (Kategoria aktualna : mojaKategoria) {
            if (nazovKategorie.equals(aktualna.getNazov()) && pouzivatelID.equals(aktualna.getPouzivatelID())) {
                vymaz = aktualna;
                break;
            }
        }

        if (vymaz != null) {
            mojaKategoria.remove(vymaz);
        }
    }

    @Override
    public Kategoria vratKategoriu(Long cislo,Long pouzivatelID) {
        Kategoria f = null;
        for (Kategoria aktualna : mojaKategoria) {
            if(aktualna.getCislo().equals(Kategoria.ineFinal))
            {
                f=aktualna;
                continue;
            }
             if(pouzivatelID==null)
            {
             continue;
            }
            if(cislo==null)
            {
                continue;
            }
            if (aktualna.getCislo().equals( cislo)&& pouzivatelID.equals(aktualna.getPouzivatelID())) {
                return aktualna;
            }

        }
        /*
        try {
            throw new NeexistujeCisloKategorieException("Zle zadané číslo kategórie, kategória s daným číslom neexistuje.");
        } catch (NeexistujeCisloKategorieException ex) {
            Logger.getLogger(PametovyKategoriaDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        return f;
    }

    @Override
    public Kategoria vratKategoriu(String nazovKategorie,Long pouzivatelID) {
        return this.vratKategoriu(this.vratCislo(nazovKategorie,pouzivatelID),pouzivatelID);
    }

    @Override
    public void upravKategoriu(Kategoria upravovanaKategoria, Long aktualneID) {
        for(Kategoria aktualna : mojaKategoria)
        {
            if(aktualna.getCislo().equals(upravovanaKategoria.getCislo()) && aktualna.getPouzivatelID().equals(aktualneID))
            {
                aktualna=upravovanaKategoria;
                break;
            }
        }
    }
}
