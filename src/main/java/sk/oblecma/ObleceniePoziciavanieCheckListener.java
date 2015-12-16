/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 *
 * @author Slavomír
 */
class ObleceniePoziciavanieCheckListener implements ActionListener{
     
    private spustac aThis;
    private Oblecenie oblecenie;
    private OblecenieDao oblecenieDao;
    private JCheckBox poziciavanieCheckBox;
    
    public ObleceniePoziciavanieCheckListener() {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        System.out.println("zmena poziciavania : "+oblecenie.getNazov());
        
        oblecenie.setMozeSaPoziciavat(poziciavanieCheckBox.isSelected());
       
        System.out.println(" "+oblecenie.isMozeSaPoziciavat());
        oblecenieDao.upravOblecenie(oblecenie);
        /*
        aThis.vykresli(aThis.vytvorMapuOblecenieObrazok(oblecenieDao.dajObleceniePodlaKategorie(oblecenie.getKategoria(), oblecenie.getVlastnikID())));
      */
    }

    void set(JCheckBox poziciavanieCheckBox,spustac aThis, Oblecenie oblecenie,OblecenieDao oblecenieDao) {
       this.aThis=aThis;
       this.oblecenie=oblecenie;
       this.oblecenieDao = oblecenieDao;
       this.poziciavanieCheckBox=poziciavanieCheckBox;
    }
}
