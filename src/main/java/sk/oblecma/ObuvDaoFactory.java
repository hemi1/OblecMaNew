/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

/**
 *
 * @author Rastislav
 */
public enum ObuvDaoFactory {
    INSTANCE;
    public ObuvDao dajObuvDao()
    {
      
        if(spustac.NapojeneNaDatabazu)
        {
          return new DatabazovyObuvDao();
        }    
        return new PametovyObuvDao();

    }

   
}
