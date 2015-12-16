/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author alfred
 */
public class ViewOblecenie {

    private final Oblecenie oblecenie;
    private final String[] statusy = {"nové", "nosené", "staré"};
    private JPanel viewPanel;
    private JCheckBox mozeSaPoziciavatCheckBox;
    private JComboBox statusComboBox;
    private JButton odstranitButton;
    
    protected JCheckBox returnPoziciavanieCheckBox()
    {
        return mozeSaPoziciavatCheckBox;
    }
    
    protected JButton odstranovacie()
    {
        return odstranitButton;
    }
    
    protected JComboBox returnStatusComboBox()
    {
        return statusComboBox;
    }
    
    public ViewOblecenie(Oblecenie oblecenie, ImageIcon obrazok) {
        this.oblecenie = oblecenie;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;

        this.viewPanel = new JPanel(new GridBagLayout());
        viewPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 200), 2, true));

        gbc.gridy = 0;
        odstranitButton = new JButton("Odstrániť oblečenie");
       /*
        odstranitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(null, "Naozaj chcete vymazať oblečenie?", "", JOptionPane.YES_NO_OPTION);

                if (answer == JOptionPane.YES_OPTION) {

                } else {

                }
            }
        });
        */
        viewPanel.add(odstranitButton, gbc);

        gbc.gridy = 1;
        JLabel obrazokLabel = new JLabel(obrazok, JLabel.CENTER);
        obrazokLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
        viewPanel.add(obrazokLabel, gbc);

        gbc.gridy = 2;
        JLabel nazovLabel = new JLabel(oblecenie.getNazov());
        nazovLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        viewPanel.add(nazovLabel, gbc);

        gbc.gridy = 3;
        JLabel popisLabel = new JLabel(this.getPopisForView());
        viewPanel.add(popisLabel, gbc);

        gbc.gridy = 4;
        this.statusComboBox = new JComboBox(this.statusy);

        if (oblecenie.isNove()) {
            statusComboBox.setSelectedItem("nové");
        } else if (oblecenie.isNosene()) {
            statusComboBox.setSelectedItem("nosené");
        } else if (oblecenie.isStare()) {
            statusComboBox.setSelectedItem("staré");
        }
        viewPanel.add(statusComboBox, gbc);

        gbc.gridy = 5;
        this.mozeSaPoziciavatCheckBox = new JCheckBox("môže sa požičiavať");
        this.mozeSaPoziciavatCheckBox.setSelected(oblecenie.isMozeSaPoziciavat());
        viewPanel.add(mozeSaPoziciavatCheckBox, gbc);

        viewPanel.repaint();
    }

    public Oblecenie getOblecenie() {
        return oblecenie;
    }

    public JPanel getViewPanel() {
        return viewPanel;
    }

    public boolean isMozeSaPoziciavatCheckBoxSelected() {
        return mozeSaPoziciavatCheckBox.isSelected();
    }

    // vracia popis oblecenia bez nazvu a toho, ci sa moze poziciavat 
    // alebo je nove/stare/nosene
    private String getPopisForView() {
        StringBuilder sb = new StringBuilder();
        // previest na html, kvoli multi-line v JLabel
        String font = "<font color=";
        String green = "'green'>";
        String cyan = "'blue'>";
        String close = "</font>";
        String riadok = "<br>";        
        sb.append("<html>");        
        
        //sb.append(oblecenie.getNazov() + "\n");
                
        if (oblecenie.isFormalne()) {
            sb.append("formálne");
            sb.append(riadok);
        } else {
            sb.append("nie je formálne");
            sb.append(riadok);
        }

        if (oblecenie.isNeprefuka()) {
            sb.append(font);
            sb.append(green);
            sb.append("neprefúka");
            sb.append(close);
            sb.append(riadok);
        } else {
            sb.append("prefúka");
            sb.append(riadok);
        }

        if (oblecenie.isNepremokave()) {
            sb.append(font);
            sb.append(green);
            sb.append("nepremokavé");
            sb.append(close);
            sb.append(riadok);
        } else {
            sb.append("premokavé");
            sb.append(riadok);
        }

        if (oblecenie.isZateplene()) {
            sb.append(font);
            sb.append(green);
            sb.append("zateplené");
            sb.append(close);
            sb.append(riadok);
        } else {
            sb.append("nezateplené");
            sb.append(riadok);
        }

        if (oblecenie.isvPrani()) {
            sb.append(font);
            sb.append(cyan);
            sb.append("momentálne je v praní");
            sb.append(close);
            sb.append(riadok);
        }
        else
        {
           sb.append(riadok);
        }

        return sb.append("</html>").toString();
    }

}
