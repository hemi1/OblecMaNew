package sk.oblecma.pokus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import sk.oblecma.Oblecenie;

/**
 *
 * @author alfred
 */
public class pokusForm extends javax.swing.JFrame {

    private final GridBagConstraints gbc;

    /**
     * Creates new form pokusForm
     */
    public pokusForm() {
        Map<Oblecenie, ImageIcon> skusobnyZoznam = new HashMap<>();

        ImageIcon obrazok = new ImageIcon("/home/alfred/oblecenie/2.jpg");

        obrazok = this.resizeImageIcon(obrazok, 160, 160);

        Oblecenie oblJeden = new Oblecenie();
        oblJeden.setNazov("JEDEN");
        oblJeden.setMozeSaPoziciavat(true);
        oblJeden.setNosene(true);
        skusobnyZoznam.put(oblJeden, obrazok);

        Oblecenie oblDva = new Oblecenie();
        oblDva.setNazov("DVA");
        oblDva.setMozeSaPoziciavat(false);
        oblDva.setStare(true);
        skusobnyZoznam.put(oblDva, obrazok);

        Oblecenie oblTri = new Oblecenie();
        oblTri.setNazov("TRI");
        skusobnyZoznam.put(oblTri, obrazok);

        Oblecenie oblStyri = new Oblecenie();
        oblStyri.setNazov("STYRI");
        skusobnyZoznam.put(oblStyri, obrazok);

        Oblecenie oblPat = new Oblecenie();
        oblPat.setNazov("PAT");
        skusobnyZoznam.put(oblPat, obrazok);

        initComponents();
        this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);

        viewPanel.setLayout(new GridBagLayout());
        this.gbc = new GridBagConstraints();
        this.gbc.insets = new Insets(5, 5, 5, 5);

        /*for (int i = 0; i < 5; i++) {
            Oblecenie nove = new Oblecenie();
            nove.setNazov("DACO");
            skusobnyZoznam.put(nove, obrazok);
        }*/
        this.vykresli(skusobnyZoznam);

        repaint();
    }

    public void vykresli(Map<Oblecenie, ImageIcon> zoznamOblecenia) {
        //int pocetStlpcov = 3;
        //int pocetRiadkov = zoznamOblecenia.keySet().size() / 3 + 1;

        int stlpec = 0;
        int riadok = 0;

        for (Oblecenie oblecenie : zoznamOblecenia.keySet()) {
            gbc.gridx = stlpec;
            gbc.gridy = riadok;

            viewPanel.add(new ViewOblecenie(oblecenie, zoznamOblecenia.get(oblecenie)).getViewPanel(), gbc);

            stlpec++;

            if (stlpec > 3) {
                stlpec = 0;
                riadok++;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        viewPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        javax.swing.GroupLayout viewPanelLayout = new javax.swing.GroupLayout(viewPanel);
        viewPanel.setLayout(viewPanelLayout);
        viewPanelLayout.setHorizontalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 373, Short.MAX_VALUE)
        );
        viewPanelLayout.setVerticalGroup(
            viewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 273, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(viewPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(pokusForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pokusForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pokusForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pokusForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pokusForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel viewPanel;
    // End of variables declaration//GEN-END:variables

    public class ViewOblecenie {

        private final Oblecenie oblecenie;
        private final String[] statusy = {"nové", "nosené", "staré"};
        private JPanel viewPanel;
        private JCheckBox mozeSaPoziciavatCheckBox;
        private JComboBox statusComboBox;
        private JButton odstranitButton;

        public ViewOblecenie(Oblecenie oblecenie, ImageIcon obrazok) {
            this.oblecenie = oblecenie;

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;

            this.viewPanel = new JPanel(new GridBagLayout());
            viewPanel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 200), 2, true));

            gbc.gridy = 0;
            odstranitButton = new JButton("Odstrániť oblečenie");
            odstranitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int answer = JOptionPane.showConfirmDialog(null, "Naozaj chcete vymazať oblečenie?", "", JOptionPane.YES_NO_OPTION);

                    if (answer == JOptionPane.YES_OPTION) {

                    } else {

                    }
                }
            });
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
            sb.append("<html>");

            //sb.append(oblecenie.getNazov() + "\n");
            if (oblecenie.isFormalne()) {
                sb.append("formálne <br>");
            } else {
                sb.append("nie je formálne <br>");
            }

            if (oblecenie.isNeprefuka()) {
                sb.append("neprefúka <br>");
            } else {
                sb.append("prefúka <br>");
            }

            if (oblecenie.isNepremokave()) {
                sb.append("nepremokavé <br>");
            } else {
                sb.append("premokavé <br>");
            }

            if (oblecenie.isZateplene()) {
                sb.append("zateplené <br>");
            } else {
                sb.append("nezateplené <br>");
            }

            if (oblecenie.isvPrani()) {
                sb.append("momentálne je v praní <br>");
            }

            return sb.append("</html>").toString();
        }

    }

    public ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {

        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(newImg);

    }

}
