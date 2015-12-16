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
public class DefaultOblecenieDao implements OblecenieDao{
    
    OblecenieDao oblecenie = OblecenieDaoFactory.instance.dajOblecenieDao();

    @Override
    public void pridajOblecenie(Oblecenie noveOblecenie, Long ID) {
           oblecenie.pridajOblecenie(noveOblecenie, ID); 
    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia(Long ID) {
       return oblecenie.dajVsetkyOblecenia(ID);
     }

    @Override
    public List<Oblecenie> dajObleceniePodlaKategorie(Long kategoria, Long ID) {
       return oblecenie.dajObleceniePodlaKategorie(kategoria, ID);
    }

    @Override
    public void vyhodOblecenie(Long idOblecenia, Long ID) {
       oblecenie.vyhodOblecenie(idOblecenia, ID);
    }

    @Override
    public void nastavKategoriuOblecenie(Long cisloOblecenia,Long cisloKategorie, Long ID) {
        oblecenie.nastavKategoriuOblecenie(cisloOblecenia, cisloKategorie, ID);
    }

    @Override
    public void odstranKategoriuOblecenia(Long cisloKategorie, Long ID) {
        oblecenie.odstranKategoriuOblecenia(cisloKategorie, ID);
    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia() {
        return oblecenie.dajVsetkyOblecenia();
    }

    @Override
    public void upravOblecenie(Oblecenie upraveneOblecenie) {
        oblecenie.upravOblecenie(upraveneOblecenie);
    }

    @Override
    public Oblecenie dajOblecenie(Long id) {
       return oblecenie.dajOblecenie(id);
    }
    
    
    
    
}
