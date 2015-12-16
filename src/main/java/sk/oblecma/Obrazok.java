package sk.oblecma;



import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public class Obrazok {
   
   private String subor;
   private Long id;

     public Obrazok() {
    }
   
    public String getSubor() {
        return subor;
    }

    public void setSubor(String subor) {
        this.subor = subor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
   
  
  
    public File getFile() {
        File sb = new File(subor);
        return sb;
    }

    
 
   
}
