/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rastislav
 */
public class DatabazovyObuvDaoTest {
    DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
    private Long IDuzivatela = Uzivatel.globalUser;
    private DatabazovyObuvDao instance = new DatabazovyObuvDao();
    public DatabazovyObuvDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of pridajObuv method, of class DatabazovyObuvDao.
     */
    @Test
    public void testPridajObuv() {
        System.out.println("pridajObuv");
         Long IDkategorie = 2L;
         String nazov = "semisky";
        Obuv novaObuv= new Obuv();
        novaObuv.setNazov(nazov);   
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        instance.pridajObuv(novaObuv,IDkategorie);
        Long ID = 3L;
        
        instance.pridajObuv(novaObuv, ID);
        Obuv obuv = instance.dajObuv(ID);
        String result = obuv.getNazov();
        
        Assert.assertEquals(nazov,result);
        
    }

    /**
     * Test of vyhodObuv method, of class DatabazovyObuvDao.
     */
    @Test
    public void testVyhodObuv() {
        System.out.println("vyhodObuv");
        Long idObuvy = 1L;
        Long ID = 1L;
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        instance.vyhodObuv(idObuvy, ID);
      
       
    }

    /**
     * Test of dajVsetkyObuvyPodlaId method, of class DatabazovyObuvDao.
     */
    @Test
    public void testDajVsetkyObuvyPodlaId() {
        System.out.println("dajVsetkyObuvyPodlaId");
        Long ID = 1L;
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        List<Obuv> expResult = null;
        List<Obuv> result = instance.dajVsetkyObuvyPodlaId(ID);
        assertNotEquals(expResult, result);
        
      
    }

    /**
     * Test of dajVsetkyObuvy method, of class DatabazovyObuvDao.
     */
    @Test
    public void testDajVsetkyObuvy() {
        System.out.println("dajVsetkyObuvy");
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        List<Obuv> expResult = null;
        List<Obuv> result = instance.dajVsetkyObuvy();
        assertNotEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of upravObuvy method, of class DatabazovyObuvDao.
     */
    @Test
    public void testUpravObuvy() {
        System.out.println("upravObuvy");
        Obuv upravenaObuv = new Obuv();
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        upravenaObuv=instance.dajObuv(1L);
        Obuv stara =instance.dajObuv(1L);
        upravenaObuv.setNazov("semisky");
        assertNotEquals(upravenaObuv, stara);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of dajObuv method, of class DatabazovyObuvDao.
     */
    @Test
    public void testDajObuv() {
        System.out.println("dajObuv");
        Long id = null;
        DatabazovyObuvDao instance = new DatabazovyObuvDao();
        Obuv expResult = null;
        Obuv result = instance.dajObuv(id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
