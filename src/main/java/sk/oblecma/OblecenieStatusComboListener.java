/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/**
 *
 * @author Slavomír
 */
class OblecenieStatusComboListener implements ActionListener{
    
    private spustac aThis;
    private Oblecenie oblecenie;
    private OblecenieDao oblecenieDao;
    private JComboBox statusComboBox;
    
    public OblecenieStatusComboListener() {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        System.out.println("zmena statusu : "+oblecenie.getNazov());
        
        String selectedItem = (String)statusComboBox.getSelectedItem();
        
               
        if(selectedItem.equals("nosené"))
        {
            oblecenie.setNove(false);
            oblecenie.setStare(false);
            oblecenie.setNosene(true);
            
        }
        if(selectedItem.equals("staré"))
        {
            oblecenie.setNove(false);
            oblecenie.setStare(true);
            oblecenie.setNosene(false);
        }
         if(selectedItem.equals("nové"))
        {
            /*
            oblecenie.setNove(true);
            oblecenie.setStare(false);
            oblecenie.setNosene(false);
            */
            if(oblecenie.isNosene())
            {
                statusComboBox.setSelectedIndex(1);
            }
            if(oblecenie.isStare())
            {
                statusComboBox.setSelectedIndex(2);
            }
        }
        
        System.out.println(" "+oblecenie.isNove());
        oblecenieDao.upravOblecenie(oblecenie);
        /*
        aThis.vykresli(aThis.vytvorMapuOblecenieObrazok(oblecenieDao.dajObleceniePodlaKategorie(oblecenie.getKategoria(), oblecenie.getVlastnikID())));
      */
    }

    void set(JComboBox statusComboBox,spustac aThis, Oblecenie oblecenie,OblecenieDao oblecenieDao) {
       this.aThis=aThis;
       this.oblecenie=oblecenie;
       this.oblecenieDao = oblecenieDao;
       this.statusComboBox = statusComboBox;
    }
    
}
