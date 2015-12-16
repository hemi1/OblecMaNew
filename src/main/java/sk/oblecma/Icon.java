package sk.oblecma;



import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
class Icon {

    private ImageIcon icon;
    private int id;
    public Icon(int id) {
        this.id=id;
    }
    
    void setImage(ImageIcon icon) {
        this.icon = icon;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return ""+id;
    }
    
    
}
