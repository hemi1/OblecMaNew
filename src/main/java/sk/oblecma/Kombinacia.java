/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author alfred
 */
public class Kombinacia {

    /*datum sa pouziva pre HistoriaDao*/
    private LocalDate datum;
    private Oblecenie[] kombinacia = new Oblecenie[24];
    private Long[] zoznamId = new Long[24];
    private int pocetObleceni;
    private Long id;
    
    
    public Kombinacia() {
    }
    
    
    
    public Kombinacia(List<Oblecenie> kombinacia) {
        napln(kombinacia);
        //this.kombinacia = kombinacia;
        this.datum = LocalDate.now();
    }

    public Kombinacia(List<Oblecenie> kombinacia, LocalDate datum) {
        napln(kombinacia);
        //this.kombinacia = kombinacia;
        this.datum = datum;
    }

    public Oblecenie[] vratKombinaciu(){
        return kombinacia;
    }
    
    public Long[] vratZoznamId() {
        return zoznamId;
    }

    public int vratPocetObleceniVKombinacii() {
        return pocetObleceni;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public static Kombinacia vratKombinaciuPodlaId(Long[] zoznamId, OblecenieDao zoznamOblecenia) {
        List<Oblecenie> kombinacia = new ArrayList<Oblecenie>();
        List<Oblecenie> vsetkoOblecenie = zoznamOblecenia.dajVsetkyOblecenia();

        for (int i = 0; i < zoznamId.length; i++) {
            for (Oblecenie oblecenie : vsetkoOblecenie) {
                if (oblecenie.getIdOblecenia().equals(zoznamId[i])) {
                    kombinacia.add(oblecenie);
                }
            }
        }

        return new Kombinacia(kombinacia);
    }

    private void napln(List<Oblecenie> kombinacia) {
        int index = 0;

        for (Oblecenie oblecenie : kombinacia) {
            this.kombinacia[index] = oblecenie;
            this.zoznamId[index] = oblecenie.getIdOblecenia();
            index++;
        }

        this.pocetObleceni = index;
    }


    public boolean jeRovnaka(Kombinacia obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kombinacia other = (Kombinacia) obj;
        
        for(int i=0; i<this.kombinacia.length; i++){
            boolean zhoda = false;
            for(int j=0; j<other.kombinacia.length; j++){
                if(this.kombinacia[i]==other.kombinacia[j]){
                    zhoda=true;
                }
            }
            if(!zhoda){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return datum.toString();
    }

    void nastavId(long id) {
       this.id = id;
    }
    
    public Long vratId()
    {
       return id;   
    } 
    

}
