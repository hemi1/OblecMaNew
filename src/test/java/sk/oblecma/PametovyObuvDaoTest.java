/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rastislav
 */
public class PametovyObuvDaoTest {
    
    public PametovyObuvDaoTest() {
    }
    
  
    /**
     * Test of pridajObuv method, of class PametovyObuvDao.
     */
    @Test
    public void testPridajObuv() {
        System.out.println("pridajObuv");
        Obuv novaObuv = new Obuv();
        novaObuv.setNazov("semisky");
        Long ID = 1L;
        PametovyObuvDao instance = new PametovyObuvDao();
        instance.pridajObuv(novaObuv, ID);
        Obuv nova = instance.dajObuv(ID);
        assertEquals(nova, novaObuv);
        
      
    }

    /**
     * Test of vyhodObuv method, of class PametovyObuvDao.
     */
    @Test
    public void testVyhodObuv() {
        System.out.println("vyhodObuv");
        Long idObuvy = 1L;
        Long ID = 1L;
        PametovyObuvDao instance = new PametovyObuvDao();
        instance.vyhodObuv(idObuvy, ID);
      
    }

    /**
     * Test of dajVsetkyObuvyPodlaId method, of class PametovyObuvDao.
     */
    @Test
    public void testDajVsetkyObuvyPodlaId() {
        System.out.println("dajVsetkyObuvyPodlaId");
        Obuv novaObuv = new Obuv();
        novaObuv.setNazov("semisky");
        Long ID = 1L;
        PametovyObuvDao instance = new PametovyObuvDao();
        instance.pridajObuv(novaObuv, ID);
       
        List<Obuv> expResult = null;
        List<Obuv> result = instance.dajVsetkyObuvyPodlaId(ID);
        assertNotEquals(expResult, result);
       
    }

    /**
     * Test of dajVsetkyObuvy method, of class PametovyObuvDao.
     */
    @Test
    public void testDajVsetkyObuvy() {
         System.out.println("dajVsetkyObuvy");
         Obuv novaObuv = new Obuv();
        novaObuv.setNazov("semisky");
        Long ID = 1L;
        PametovyObuvDao instance = new PametovyObuvDao();
        instance.pridajObuv(novaObuv, ID);
  
        List<Obuv> expResult = null;
        List<Obuv> result = instance.dajVsetkyObuvy();
        assertNotEquals(expResult, result);
      
    }

    /**
     * Test of upravObuvy method, of class PametovyObuvDao.
     */
    @Test
    public void testUpravObuvy() {
        System.out.println("upravObuvy");
        Obuv upravenaObuv = null;
        PametovyObuvDao instance = new PametovyObuvDao();
        instance.upravObuvy(upravenaObuv);
        
    }

    /**
     * Test of dajObuv method, of class PametovyObuvDao.
     */
    @Test
    public void testDajObuv() {
        System.out.println("dajObuv");
        Long id = null;
        PametovyObuvDao instance = new PametovyObuvDao();
        Obuv expResult = null;
        Obuv result = instance.dajObuv(id);
        assertEquals(expResult, result);
       
    }
    
}
