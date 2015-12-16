/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotove testy
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Slavomír
 */
public class PametovyObrazokDao implements ObrazokDao {
    
    List<Obrazok> obrazky =new ArrayList<>();
    
    public PametovyObrazokDao() {
    }

    @Override
    public List<Obrazok> zistiData() {
        return obrazky;
    }

    @Override
    public void pridajData(File file) {
        Obrazok novy = new Obrazok();
        novy.setSubor(file.getAbsolutePath());
        
        Long id = null;
        id = (long)obrazky.size()+1;
        novy.setId(id);
        
        obrazky.add(novy);
    }
    
}
