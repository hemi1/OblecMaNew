/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rastislav
 */
public class PametovyObuvDao implements ObuvDao{

     private List<Obuv> obuv = new ArrayList<>();
     
     public PametovyObuvDao(){}
     
    @Override
    public void pridajObuv(Obuv novaObuv, Long ID) {
        novaObuv.setVlastnikID(ID);
        if(novaObuv.getIdObuvy()== null)
        {
            novaObuv.setIdObuvy(obuv.size()+1);
        }
        obuv.add(novaObuv);
    }

    @Override
    public void vyhodObuv(Long idObuvy, Long ID) {
            Obuv vymaz = null;
        
        for(Obuv aktualne : obuv)
        {
            int idObuv= Math.toIntExact(aktualne.getIdObuvy());
            if(idObuvy == idObuv)
            {
                if(aktualne.getVlastnikID().equals(ID))
                    vymaz = aktualne;
                break;
            } else {
            }
        }
        if(vymaz!=null)
        obuv.remove(vymaz);    }

    @Override
    public List<Obuv> dajVsetkyObuvyPodlaId(Long ID) {
   List<Obuv> zoznam = new ArrayList<>();
        
        for(Obuv aktualne : obuv)
        {
            if(aktualne.getVlastnikID().equals(ID))
            zoznam.add(aktualne);
        }
        
        return zoznam; }

    @Override
    public List<Obuv> dajVsetkyObuvy() {
        return obuv;
    }

    @Override
    public void upravObuvy(Obuv upravenaObuv) {
        for(Obuv aktualne : obuv)
       {
           if(aktualne.getIdObuvy().equals(upravenaObuv.getIdObuvy()))
           {
               aktualne=upravenaObuv;
               return;
           }
       }
    }

    @Override
    public Obuv dajObuv(Long id) {
         Obuv vrat = null;
       for(Obuv aktualne : obuv)
       {
           if(aktualne.getIdObuvy().equals(id))
           {
               vrat=aktualne;
               break;
           }
       }
       return vrat;
    }
    
}
