package sk.oblecma;


// hotove testy
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
public class PametovyUzivatelDao implements UzivatelDao {

     private List<Uzivatel> zoznamUzivatelov;
    
    private Uzivatel prihlasenyUzivatel;
    
    public PametovyUzivatelDao() {
    zoznamUzivatelov = new ArrayList<>();
    prihlasenyUzivatel = null;
    
         try {
             vytvorPouzivatela("meno", "heslo",true);
         } catch (Exception ex) {
             System.out.println("Chyba nastavenia - v programe to tu nebude");
         }
    }
       
    
    @Override
    public void vytvorPouzivatela(String meno, String heslo,boolean muz) throws Exception{
        
        for(Uzivatel uzivatel : zoznamUzivatelov)
        {
            if(uzivatel.getMeno().toLowerCase().equals(meno.toLowerCase()))
                throw new UzivatelExistujeException();
        }
        
        Uzivatel novyUzivatel = new Uzivatel(meno,heslo,muz);
        
        int id = zoznamUzivatelov.size()+1;
        System.out.print("vytvaram "+novyUzivatel.getMeno()+" s "+id);
        Long noveID = (long) id;
        
        novyUzivatel.setId(noveID);
        zoznamUzivatelov.add(novyUzivatel);
    }

    @Override
    public void prihlasPouzivatela(String meno, String heslo)throws Exception{
        if(prihlasenyUzivatel!=null)
        {
            throw new JePrihlasenyInyUzivatelException();
        }
       for(Uzivatel uzivatel : zoznamUzivatelov)
       {
           if(meno.equals(uzivatel.getMeno()))
           {
               if(heslo.equals(uzivatel.getHeslo()))
               {
                   prihlasenyUzivatel=uzivatel;
                   return;
               }
               else
               {
                   throw new  NieJePrihlasenyZiadenPouzivatelException();
               }
           }
       }
       //prihlasenyUzivatel=null;
       throw new NieJePrihlasenyZiadenPouzivatelException();
       
    }

    @Override
    public void odhlasPouzivatela() throws Exception{
       if(prihlasenyUzivatel==null)
          throw new NieJePrihlasenyZiadenPouzivatelException();
       
       prihlasenyUzivatel=null;
       
     }

    @Override
    public boolean jePrihlasny(String meno) {
        
        if(meno==null || prihlasenyUzivatel==null)
        {
            return false;
        }
        
      if(meno.equals(prihlasenyUzivatel.getMeno()))
        return true;
      
      return false;
    }

    @Override
    public Long vratIdPrihlasenehoPouzivatela() throws Exception{
        
        if(prihlasenyUzivatel==null)
        {
            throw new NieJePrihlasenyZiadenPouzivatelException();
        }
        
        return prihlasenyUzivatel.getId();
    }

    @Override
    public List<Long> vratIdVsetkychUzivatelov() {
        List<Long> id = new ArrayList<>();
        
        for(Uzivatel aktualny : zoznamUzivatelov)
        {
            id.add(aktualny.getId());
        }
        
        return id;
    }

    @Override
    public boolean jeMuz(Long id) {
        return prihlasenyUzivatel.muz();
    }

    @Override
    public boolean jeZena(Long id) {
        return prihlasenyUzivatel.zena();
    }
    
}
