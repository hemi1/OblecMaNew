/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Slavomír
 */
public class PicturesFileFilter extends FileFilter{

    @Override
    public boolean accept(File f) {
       boolean accept = false;
        if(f.isDirectory())
        {
            return true;
        }
        
      String name = f.getName();
      
      if(name.endsWith(".png") || name.endsWith(".jpg"))
      {
          return true;
      }
        
        return accept;
    }

    @Override
    public String getDescription() {
        return "picture (.jpg .png)";
    }
    
}
