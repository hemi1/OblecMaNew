package sk.oblecma;
// hotove testy


import static java.sql.JDBCType.NULL;
import java.util.ArrayList;
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
public class PametoveOblecenieDao implements OblecenieDao {
    
    private List<Oblecenie> oblecenie = new ArrayList<>();
    
    public PametoveOblecenieDao() {
    }

    @Override
    public void pridajOblecenie(Oblecenie noveOblecenie,Long ID) {
        noveOblecenie.setVlastnikID(ID);
        if(noveOblecenie.getIdOblecenia() == null)
        {
            noveOblecenie.setIdOblecenia(oblecenie.size()+1);
        }
        oblecenie.add(noveOblecenie);
    }
    
    @Override
    public void vyhodOblecenie(Long idOblecenia, Long ID) {
        
        Oblecenie vymaz = null;
        
        for(Oblecenie aktualne : oblecenie)
        {
            if(idOblecenia.equals(aktualne.getIdOblecenia()))
            {
                if(aktualne.getVlastnikID().equals(ID))
                vymaz = aktualne;
                break;
            }
        }
        if(vymaz!=null)
        oblecenie.remove(vymaz);
    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia(Long ID) {
        List<Oblecenie> zoznam = new ArrayList<>();
        
        for(Oblecenie aktualne : oblecenie)
        {
            if(aktualne.getVlastnikID().equals(ID))
            zoznam.add(aktualne);
        }
        
        return zoznam;
    }

    @Override
    public List<Oblecenie> dajObleceniePodlaKategorie(Long kategoria, Long ID) {
        List<Oblecenie> zoznam = new ArrayList<>();
        
        for(Oblecenie aktualne : oblecenie)
        {
            if(aktualne.getVlastnikID().equals(ID))
            {
                if(aktualne.getKategoria().equals(kategoria))
                {
                    zoznam.add(aktualne);
                }
            }
        }
        return zoznam;
    }

    @Override
    public void odstranKategoriuOblecenia(Long cisloKategorie, Long ID) {
        for(Oblecenie aktualne : oblecenie)
        {
            if(aktualne.getKategoria().equals(cisloKategorie) && aktualne.getVlastnikID().equals(ID))
            {
                aktualne.setKategoria(Kategoria.ineFinal);
            }
        }
    }

    @Override
    public void nastavKategoriuOblecenie(Long cisloOblecenia ,Long cisloKategorie, Long ID) {
        for(Oblecenie aktualne : oblecenie)
        {
            if(aktualne.getIdOblecenia().equals(cisloOblecenia) &&aktualne.getKategoria().equals(Kategoria.ineFinal) && aktualne.getVlastnikID().equals(ID))
            {
                aktualne.setKategoria(cisloKategorie);
            }
        }
    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia() {
        return oblecenie;
    }

    @Override
    public void upravOblecenie(Oblecenie upraveneOblecenie) {
         for(Oblecenie aktualne : oblecenie)
       {
           if(aktualne.getIdOblecenia().equals(upraveneOblecenie.getIdOblecenia()))
           {
               aktualne=upraveneOblecenie;
               return;
           }
       }
    }

    @Override
    public Oblecenie dajOblecenie(Long id) {
        Oblecenie vrat = null;
       for(Oblecenie aktualne : oblecenie)
       {
           if(aktualne.getIdOblecenia().equals(id))
           {
               vrat=aktualne;
               break;
           }
       }
       return vrat;
    }

   
    
}
