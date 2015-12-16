/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.Color;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Slavomír
 */
public class OblecMaListCellRenderer extends DefaultListCellRenderer{
    
    private void NastavPozicanie (Component komponent)
    {
        komponent.setForeground(new Color(0,0,255));
    }
    
    private void NastavNormalne(Component komponent)
    {
        komponent.setForeground(new Color(0,0,0));
    }        
    
    private void NastavJeVPrani(Component komponent)
    {
        komponent.setForeground(new Color(255,0,255));
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
         String text;
        Component oblecenieKomponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
       
        Oblecenie oblecenie = (Oblecenie) value;
        
        NastavNormalne(oblecenieKomponent);
       
        if(oblecenie.isvPrani())
        {
            NastavJeVPrani(oblecenieKomponent);
        }
        
       if(!oblecenie.getVlastnikID().equals(spustac.aktualneID))
       {
          NastavPozicanie(oblecenieKomponent);
       }
       
        
        return oblecenieKomponent;
    }
    
}
