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
public interface OblecenieDao {

    public void pridajOblecenie(Oblecenie noveOblecenie, Long ID);

    public void vyhodOblecenie(Long idOblecenia, Long ID);

    public List<Oblecenie> dajVsetkyOblecenia(Long ID);

    public List<Oblecenie> dajVsetkyOblecenia();

    public List<Oblecenie> dajObleceniePodlaKategorie(Long kategoria, Long ID);

    public void nastavKategoriuOblecenie(Long cisloOblecenia, Long cisloKategorie, Long ID);

    public void odstranKategoriuOblecenia(Long cisloKategorie, Long ID);

    public void upravOblecenie(Oblecenie upraveneOblecenie);
    
    public Oblecenie dajOblecenie(Long id);
}
