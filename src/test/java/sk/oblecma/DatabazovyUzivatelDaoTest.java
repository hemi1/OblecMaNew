/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;



/**
 *
 * @author Slavomír
 */
public class DatabazovyUzivatelDaoTest {
    
    DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
    private static DatabazovyUzivatelDao instance;
    public DatabazovyUzivatelDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
      instance = new DatabazovyUzivatelDao();
    }
    
    @AfterClass
    public static void tearDownClass() {
     DatabazaSpojenie.jdbcTemplate.execute("delete from pouzivatel where id > 1");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
     //   DatabazaSpojenie.jdbcTemplate.execute("delete table pouzivatel");
    }

    /**
     * Test of vytvorPouzivatela method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testVytvorPouzivatela() throws Exception {
        System.out.println("vytvorPouzivatela");
        String meno = "a";
        String heslo = "b";
        boolean muz = false;
        
        boolean stav = true;
        
        try{
         instance.odhlasPouzivatela();
        }catch(Exception e)
        {
            
        }
        
        try{
        instance.vytvorPouzivatela(meno, heslo, muz);
        }
        catch(UzivatelExistujeException e)
        {
            stav=true;
        }
        if(stav)
        {
        try{
        instance.vytvorPouzivatela(meno, heslo, muz);
        stav=false;
        }
        catch(UzivatelExistujeException e)
        {
            System.out.println("Opetovne pridanie sa nepodarilo OK");
        }
        }

        instance.prihlasPouzivatela(meno, heslo);
        instance.odhlasPouzivatela();
        
    // TODO review the generated test code and remove the default call to fail.
        assertEquals(true,stav);
    }

    /**
     * Test of prihlasPouzivatela method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testPrihlasPouzivatela() throws Exception {
        System.out.println("prihlasPouzivatela");
        String meno = "meno";
        String heslo = "heslo";
        String nespravneHeslo = "heslo2";
        String nespravneMeno = "Meno";
        
        boolean ok =true;

        try {
           instance.vytvorPouzivatela(meno, heslo, true);
            instance.prihlasPouzivatela(meno, heslo);
            instance.odhlasPouzivatela();
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            ok =false; System.out.println("Chyba");
        }catch(UzivatelExistujeException e)
                {
                    
                }
        
        
         try {
       //     instance.vytvorPouzivatela(meno, nespravneHeslo, true);
            instance.prihlasPouzivatela(meno, nespravneHeslo);
           
            ok = false;
             System.out.println("Peslo zle heslo");
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
             System.out.println("Zle");
        }
        
           try {
               
       //     instance.vytvorPouzivatela(nespravneMeno, heslo, true);
            instance.prihlasPouzivatela(nespravneMeno, heslo);
           
            ok = false;
             System.out.println("Preslo zle meno");
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            System.out.println("Zle");
        }

        assertEquals(true, ok);
        
    }

    /**
     * Test of odhlasPouzivatela method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testOdhlasPouzivatela() throws Exception {
        System.out.println("odhlasPouzivatela");
        
        String meno ="baf";
        String heslo="haf";
                
       
        instance.vytvorPouzivatela(meno, heslo, true);
        instance.prihlasPouzivatela(meno, heslo);
        boolean uspesnePrihlasene=instance.jePrihlasny(meno);
        if(uspesnePrihlasene==false)
        {
            System.err.println("nepodarilo sa ani prihlasit pouzivatela");
            assertEquals(1, 0);
            return;
        }
        instance.odhlasPouzivatela();

        try{
        instance.prihlasPouzivatela(meno, heslo);
        }
        catch(JePrihlasenyInyUzivatelException e)
        {
            // nepodarilo sa odhlasovanie
            assertEquals(1, 0);
            return;
        }
        assertEquals(1, 1);
        
        
    }

    /**
     * Test of jePrihlasny method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testJePrihlasny() {
        System.out.println("jePrihlasny");
        String meno = "meno";
 
        boolean expResult = false;
        boolean result = instance.jePrihlasny(meno);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of vratIdPrihlasenehoPouzivatela method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testVratIdPrihlasenehoPouzivatela() throws Exception {
        System.out.println("vratIdPrihlasenehoPouzivatela");
        
        String meno = "meno";
        String heslo = "heslo";
        
        Long expResult = 1L;
        Long result=0L;
         try {
            
            instance.odhlasPouzivatela();         
            
        } catch (NieJePrihlasenyZiadenPouzivatelException e) {
           System.out.println("Nepodarilo sa odhlasit ");
        }
        try {
            
            instance.prihlasPouzivatela(meno, heslo);           
            
        } catch (NieJePrihlasenyZiadenPouzivatelException e) {
           System.out.println("Nepodarilo sa prihlasit ");
        }
         try {
            
          result = instance.vratIdPrihlasenehoPouzivatela();
           
        } catch (NieJePrihlasenyZiadenPouzivatelException e) {
           System.out.println("Nepodarilo sa vratit id ");
        }
          
         try{
                      instance.odhlasPouzivatela();
         }catch(Exception e)
         {
             
         }
 
          assertEquals(expResult, result);
    
    }

    /**
     * Test of vratIdVsetkychUzivatelov method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testVratIdVsetkychUzivatelov() {
        System.out.println("vratIdVsetkychUzivatelov");
        
        List<Long> result = instance.vratIdVsetkychUzivatelov();
        
        int size  = result.size();
        
        assertEquals(2, size);
       
    }

    /**
     * Test of jeMuz method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testJeMuz() {
        System.out.println("jeMuz");
        Long id = 1L;
        boolean expResult = true;
        boolean result = instance.jeMuz(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of jeZena method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testJeZena() {
        System.out.println("jeZena");
        Long id = 1L;
        boolean expResult = false;
        boolean result = instance.jeZena(id);
        assertEquals(expResult, result);
    }
    
}
