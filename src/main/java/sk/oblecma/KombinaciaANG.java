/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Slavomír
 */
public class KombinaciaANG {
    private Long kombinacia[];
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    KombinaciaANG(int s) {
    //    System.out.println("Zacian inicializacia");
        kombinacia = new Long[24];
        
        for(int i=0;i<24;i++)
        {
            kombinacia[i]=0L;
            
        }
    //    System.out.println("Inicializacia");
    }

    KombinaciaANG() {
        kombinacia = new Long[24];
    }
    
    public void KombinaciaANG(int s)
    {
    //    System.out.println("Zacian inicializacia");
        kombinacia = new Long[24];
        
        for(int i=0;i<24;i++)
        {
            kombinacia[i]=0L;
            
        }
     //   System.out.println("Inicializacia");
    }

    public Long[] getKombinacia() {
        return kombinacia;
    }

    public void setKombinacia(Long[] kombinacia) {
        this.kombinacia = kombinacia;
    }
    public void setKombinacia(int i, Long id)
    {
        if(kombinacia==null)
        {
     //       System.out.println("CHYBAAAAAAAAAAAAAAAAa");
            return;
        }
         this.kombinacia[i]=id;   
    }

   public void sort() {
        List<Long> sort = new ArrayList<>();
        
        for(int i=0;i<kombinacia.length;i++)
        {
            sort.add(kombinacia[i]);
        }
        
        Collections.sort(sort);
        Collections.reverse(sort);
        int i=0;
        for(Long l : sort)
        {
            kombinacia[i]=l;
            i++;
        }
        
    }

    @Override
    public String toString() {
        return "kombinacia "+id;
    }
    
    
}
