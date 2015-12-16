/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Slavomír
 */
class OblecenieOdstranButtonListener implements ActionListener {

    private spustac aThis;
    private Oblecenie oblecenie;
    private OblecenieDao oblecenieDao;
    public OblecenieOdstranButtonListener() {
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        System.out.println("klik : "+oblecenie.getNazov());
        
         int answer = JOptionPane.showConfirmDialog(aThis, "Naozaj chcete vymazať oblečenie?", "", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {
                        oblecenieDao.vyhodOblecenie(oblecenie.getIdOblecenia(), oblecenie.getVlastnikID());
                        aThis.vykresli(aThis.vytvorMapuOblecenieObrazok(oblecenieDao.dajObleceniePodlaKategorie(oblecenie.getKategoria(), oblecenie.getVlastnikID())));
                } else {

                }
        

    }

    void set(spustac aThis, Oblecenie oblecenie,OblecenieDao oblecenieDao) {
       this.aThis=aThis;
       this.oblecenie=oblecenie;
       this.oblecenieDao = oblecenieDao;
    }
    
}
