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
public class DefaultOdporucaneZakazaneKombinacieDao implements OdporucenaZakazanaKombinaciaDao {
    
    private OdporucenaZakazanaKombinaciaDao kombinacie = OdporucenaZakazanaKombinaciaDaoFactory.INSTANCE.dajodOdporucenaKombinaciaDao();
    
    public DefaultOdporucaneZakazaneKombinacieDao() {
    }

    @Override
    public void OdporucKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost, Long idu) {
        kombinacie.OdporucKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost,idu);
    }

    @Override
    public List<KombinaciaANG> vratOdporuceneKombinacie(Pocasie podmienkyPocasia, boolean formalnost, Long idu) {
        return kombinacie.vratOdporuceneKombinacie(podmienkyPocasia, formalnost,idu);
    }

    @Override
    public void ZakazKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost, Long idu) {
        kombinacie.ZakazKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost,idu);
    }

    @Override
    public List<KombinaciaANG> vratZakazaneKombinacie(Pocasie podmienkyPocasia, boolean formalnost, Long idu) {
        return kombinacie.vratZakazaneKombinacie(podmienkyPocasia, formalnost,idu);
    }

    @Override
    public List<KombinaciaANG> vratVsetkyOdporuceneKombinacie(Long idUzivatela) {
        return kombinacie.vratVsetkyOdporuceneKombinacie(idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratVsetkyZakazaneKombinacie(Long idUzivatela) {
        return kombinacie.vratVsetkyZakazaneKombinacie(idUzivatela);
    }

    @Override
    public void presunDoZakazanej(Long idKombinacia) {
        kombinacie.presunDoZakazanej(idKombinacia);
    }

    @Override
    public void presunDoOdporucanej(Long idKombinacia) {
        kombinacie.presunDoOdporucanej(idKombinacia);
    }

    @Override
    public void odstranOdporucanu(Long idKombinacia) {
        kombinacie.odstranOdporucanu(idKombinacia);
    }

    @Override
    public void odstranZakazanu(Long idKombinacia) {
       kombinacie.odstranZakazanu(idKombinacia);
    }

    @Override
    public Pocasie vratPocasiePreKombinaciu(Long idKombinacia) {
      return kombinacie.vratPocasiePreKombinaciu(idKombinacia);
    }
    
}
