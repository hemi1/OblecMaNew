package sk.oblecma;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
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
public class PametovyUzivatelDaoTest {
            String meno = "meno";
        String heslo = "heslo";
      static   PametovyUzivatelDao instance;
    public PametovyUzivatelDaoTest() throws Exception {
       
    }
    

    @BeforeClass
    public static void setUpClass() {
       instance= new PametovyUzivatelDao();
    }
    
    @AfterClass
    public static void tearDownClass() {
       
    }
    
    @Before
    public void setUp() throws Exception {

    }
    
    @After
    public void tearDown() {
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
        }
        catch(Exception e)
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
            instance.odhlasPouzivatela();
           instance.vytvorPouzivatela(meno, heslo, true);
            instance.prihlasPouzivatela(meno, heslo);
            instance.odhlasPouzivatela();
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            ok =false;
        }catch(UzivatelExistujeException e)
                {
                    
                }
        
        
         try {
               if(ok){
            instance.prihlasPouzivatela(meno, nespravneHeslo);
               } instance.odhlasPouzivatela();
           
            ok = false;
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            
        }
         catch(UzivatelExistujeException e)
                {
                    
                }
        
           try {
            instance.odhlasPouzivatela();   

            instance.prihlasPouzivatela(nespravneMeno, heslo);
            instance.odhlasPouzivatela();
            ok = false;
        } catch (NieJePrihlasenyZiadenPouzivatelException ex) {
            
        }catch(UzivatelExistujeException e)
                {
                    
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
    public void testJePrihlasny() throws Exception {
        System.out.println("jePrihlasny");
        String meno = "meno";
        String heslo = "heslo";
        
        instance.prihlasPouzivatela(meno, heslo);
        boolean expResult = true;
        boolean result = instance.jePrihlasny(meno);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of vratIdPrihlasenehoPouzivatela method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testVratIdPrihlasenehoPouzivatela() throws Exception {
        System.out.println("vratIdPrihlasenehoPouzivatela");
        

        
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
    public void testJeMuz() throws Exception {
        System.out.println("jeMuz");
        Long id = 1L;
instance.prihlasPouzivatela(meno, heslo);
        boolean expResult = true;
        boolean result = instance.jeMuz(id);
        instance.odhlasPouzivatela();
        assertEquals(expResult, result);
    }

    /**
     * Test of jeZena method, of class DatabazovyUzivatelDao.
     */
    @Test
    public void testJeZena() throws Exception {
        System.out.println("jeZena");
        Long id = 1L;
        instance.prihlasPouzivatela(meno, heslo);
        boolean expResult = false;
        boolean result = instance.jeZena(id);
        instance.odhlasPouzivatela();
        assertEquals(expResult, result);
    }
    
}
