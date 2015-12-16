/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Slavomír
 */
public class BezOznaceniaJList extends JLabel implements ListCellRenderer<Object>{

    @Override
    public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
           String text;
        if(value==null)
        {
            text="(vyhodene oblecenie)";
        }
        else
        {
            text = value.toString();
        }
     
        setText(text);
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setBackground(new Color(255,255,255));
        setOpaque(true);
        return this;
    }
    
    
    
}
