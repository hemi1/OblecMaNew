/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotove testy
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Slavomír
 */
public class PametovaOdporucanaZakazanaKombinaciaDao implements OdporucenaZakazanaKombinaciaDao {
 
    Map<Integer,KombinaciaANG> kombinacia  = new HashMap();
    Map<Integer,Pocasie> pocasie = new HashMap();
    Map<Integer,Long> uzivatel = new HashMap();
    Map<Integer,Boolean> formalnost = new HashMap();
    Map<Integer,Boolean> povolena = new HashMap();
    
    private boolean odporucane;
    
    private int vlozKombinaciu(KombinaciaANG novakombinacia, Pocasie podmienkyPocasia, boolean formalne, Long idUizvatela)
    {
        int id = kombinacia.size()+1;
        novakombinacia.setId((long)id);
        
        kombinacia.put(id, novakombinacia);
        pocasie.put(id, podmienkyPocasia);
        uzivatel.put(id, idUizvatela);
        formalnost.put(id, formalne);
        
        return id;
    }
    
    private List<KombinaciaANG> vratKombinacie(Pocasie podmienkyPocasia, boolean formalne, Long idUzivatela)
    {
        List<KombinaciaANG> vrat = new ArrayList<>();
        for(Integer id : pocasie.keySet())
        {
            if(pocasie.get(id)==podmienkyPocasia)
            {
                if(uzivatel.get(id)==idUzivatela.intValue())
                {
                    if(formalnost.get(id)==formalne)
                    {
                        if(povolena.get(id)==odporucane)
                        {
                            vrat.add(kombinacia.get(id));
                        }
                    }
                }
            }
        }
        return vrat;
    }
    
    private void presun(int idKombinacia){
        odporucane=!odporucane;
        povolena.put(idKombinacia, odporucane);
    }
    
    private void odstran(int id) {
        kombinacia.remove(id);
        pocasie.remove(id);
        povolena.remove(id);
        uzivatel.remove(id);
    }
    
    private List<KombinaciaANG> vratVsetky(int intValue) {
        List<KombinaciaANG> vrat = new ArrayList<>();
        
         for(Integer id : povolena.keySet())
        {
           if(povolena.get(id)==odporucane)
           {
              vrat.add(kombinacia.get(id));
           }
        }
        
        return vrat;
    }
    
    @Override
    public void OdporucKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost, Long idUzivatela) {
           List<KombinaciaANG> vratene = vratOdporuceneKombinacie(podmienkyPocasia, formalnost, idUzivatela);
        if(vratene.isEmpty()==false)
        {   
              Long[] kombinacia1 = odporucenaKombinacia.getKombinacia();
            for(KombinaciaANG aktualna : vratene)
            {
                Long[] kombinacia2 = aktualna.getKombinacia();
                boolean ok = false;
                for(int a=0;a<kombinacia1.length;a++)
                {
                    if(!kombinacia1[a].equals(kombinacia2[a]))
                    {
                        ok=true;
                    }           
                }
                 if(!ok)
                    {
                        return;
                    }
            }
        }
        vratene = vratZakazaneKombinacie(podmienkyPocasia, formalnost, idUzivatela);
        if(vratene.isEmpty()==false)
        {   
              Long[] kombinacia1 = odporucenaKombinacia.getKombinacia();
            for(KombinaciaANG aktualna : vratene)
            {
                Long[] kombinacia2 = aktualna.getKombinacia();
                boolean ok = false;
                for(int a=0;a<kombinacia1.length;a++)
                {
                    if(!kombinacia1[a].equals(kombinacia2[a]))
                    {
                        ok=true;
                    }           
                }
                 if(!ok)
                    {
                        return;
                    }
            }
        }
        int id = vlozKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost, idUzivatela);
        povolena.put(id, true);
    }

    @Override
    public void ZakazKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost, Long idUzivatela) {
          List<KombinaciaANG> vratene = vratOdporuceneKombinacie(podmienkyPocasia, formalnost, idUzivatela);
        if(vratene.isEmpty()==false)
        {   
              Long[] kombinacia1 = odporucenaKombinacia.getKombinacia();
            for(KombinaciaANG aktualna : vratene)
            {
                Long[] kombinacia2 = aktualna.getKombinacia();
                boolean ok = false;
                for(int a=0;a<kombinacia1.length;a++)
                {
                    if(!kombinacia1[a].equals(kombinacia2[a]))
                    {
                        ok=true;
                    }           
                }
                 if(!ok)
                    {
                        return;
                    }
            }
        }
        vratene = vratZakazaneKombinacie(podmienkyPocasia, formalnost, idUzivatela);
        if(vratene.isEmpty()==false)
        {   
              Long[] kombinacia1 = odporucenaKombinacia.getKombinacia();
            for(KombinaciaANG aktualna : vratene)
            {
                Long[] kombinacia2 = aktualna.getKombinacia();
                boolean ok = false;
                for(int a=0;a<kombinacia1.length;a++)
                {
                    if(!kombinacia1[a].equals(kombinacia2[a]))
                    {
                        ok=true;
                    }           
                }
                 if(!ok)
                    {
                        return;
                    }
            }
        }
        
        int id = vlozKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost, idUzivatela);
        povolena.put(id, false);
    }

    @Override
    public List<KombinaciaANG> vratOdporuceneKombinacie(Pocasie podmienkyPocasia, boolean formalnost, Long idUzivatela) {
        odporucane=true;
        return vratKombinacie(podmienkyPocasia, formalnost, idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratZakazaneKombinacie(Pocasie podmienkyPocasia, boolean formalnost, Long idUzivatela) {
        odporucane=false;
        return vratKombinacie(podmienkyPocasia, formalnost, idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratVsetkyOdporuceneKombinacie(Long idUzivatela) {
       odporucane=true;
       return vratVsetky(idUzivatela.intValue());
    }

    @Override
    public List<KombinaciaANG> vratVsetkyZakazaneKombinacie(Long idUzivatela) {
      odporucane=false;
       return vratVsetky(idUzivatela.intValue());
    }

    @Override
    public void presunDoZakazanej(Long idKombinacia) {
        odporucane=true;
        presun(idKombinacia.intValue());
    }

    @Override
    public void presunDoOdporucanej(Long idKombinacia) {
         odporucane=false;
        presun(idKombinacia.intValue());
    }

    @Override
    public void odstranOdporucanu(Long idKombinacia) {
       odstran(idKombinacia.intValue());
    }

    @Override
    public void odstranZakazanu(Long idKombinacia) {
         odstran(idKombinacia.intValue());
    }

    @Override
    public Pocasie vratPocasiePreKombinaciu(Long idKombinacia) {
        return pocasie.get(idKombinacia.intValue());
    }

  

    

    
}
