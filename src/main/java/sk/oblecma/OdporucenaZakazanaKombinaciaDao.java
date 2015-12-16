/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.List;

/**
 *
 * @author Slavomír
 */
public interface OdporucenaZakazanaKombinaciaDao {
    public void OdporucKombinaciu(KombinaciaANG odporucenaKombinacia,Pocasie podmienkyPocasia, boolean formalnost,Long idUizvatela);
    public void ZakazKombinaciu(KombinaciaANG odporucenaKombinacia,Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela);
    public List<KombinaciaANG> vratOdporuceneKombinacie(Pocasie podmienkyPocasia,boolean formalnost,Long idUzivatela);
    public List<KombinaciaANG> vratZakazaneKombinacie(Pocasie podmienkyPocasia,boolean formalnost,Long idUzivatela);
    public List<KombinaciaANG> vratVsetkyOdporuceneKombinacie(Long idUzivatela);
    public List<KombinaciaANG> vratVsetkyZakazaneKombinacie(Long idUzivatela);
    public void presunDoZakazanej(Long idKombinacia);
    public void presunDoOdporucanej (Long idKombinacia);
    public void odstranOdporucanu (Long idKombinacia);
    public void odstranZakazanu(Long idKombinacia);
    
    public Pocasie vratPocasiePreKombinaciu(Long idKombinacia);
}
