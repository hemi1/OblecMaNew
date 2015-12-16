/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
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
 * @author Slavomír
 */
public class PametovyKategoriaDaoTest {
    
    private PametovyKategoriaDao instance;
    private Long aktualneID= Uzivatel.globalUser;
    public PametovyKategoriaDaoTest() {
        instance = new PametovyKategoriaDao();
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
     * Test of vratNazov method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratNazov() {
        System.out.println("vratNazov");
        Long cislo = 0L;

        String expResult = "ponozky";
        String result = instance.vratNazov(cislo,aktualneID);
        assertEquals(expResult, result);
    
    }

    /**
     * Test of vratCislo method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratCislo() {
        System.out.println("vratCislo");
        String nazov = "tricko";

        Long expResult = 1L;
        Long result = instance.vratCislo(nazov,aktualneID);
        assertEquals(expResult, result);

    }

    /**
     * Test of vratPocet method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratPocet() {
        System.out.println("vratPocet");

        int expResult = 5;
        int result = instance.vratPocet(aktualneID);
        assertEquals(expResult, result);

    }

    /**
     * Test of pridajKategoriu method, of class PametovyKategoriaDao.
     */
    @Test
    public void testPridajKategoriu() {
        System.out.println("pridajKategoriu");
        
        Kategoria kategoria = new Kategoria((long)instance.vratPocet(aktualneID), "nova", Kategoria.RUKY, 0,Uzivatel.globalUser,10);
        int stare = instance.vratPocet(aktualneID);
        instance.pridajKategoriu(kategoria,aktualneID);
        
        assertEquals(instance.vratPocet(aktualneID), stare+1);
    }

    /**
     * Test of vratZoznamKategorii method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratZoznamKategorii() {
        System.out.println("vratZoznamKategorii");
        
        for(Kategoria aktualna :  instance.vratZoznamKategorii(aktualneID))
        {
            System.out.println(aktualna.getNazov());
        }
        
        List<Kategoria> result = instance.vratZoznamKategorii(aktualneID);
        assertNotNull(result);
       
    }

    /**
     * Test of odstranKategoriu method, of class PametovyKategoriaDao.
     */
    @Test
    public void testOdstranKategoriu() {
        System.out.println("odstranKategoriu");
        String nazovKategorie = "nohavice";

        instance.odstranKategoriu(nazovKategorie,aktualneID);
        Long cislo =instance.vratCislo(nazovKategorie,aktualneID);
       
    }

    /**
     * Test of vratKategoriu method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratKategoriu_Long_Long() {
        System.out.println("vratKategoriu");
        Long cislo = 4L;
        Long pouzivatelID = Uzivatel.globalUser;

        Kategoria expResult = new Kategoria(Kategoria.ineFinal, "ine", 0, 0, Uzivatel.globalUser, 0);
        expResult = new Kategoria(cislo, "tricko12", Kategoria.TELO, 1,pouzivatelID, 3);
        instance.pridajKategoriu(expResult, pouzivatelID);
        cislo = expResult.getCislo();
        Kategoria result = instance.vratKategoriu(cislo, pouzivatelID);
                
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of vratKategoriu method, of class PametovyKategoriaDao.
     */
    @Test
    public void testVratKategoriu_String_Long() {
        System.out.println("vratKategoriu");
       Long cislo = 6L;
        Long pouzivatelID = Uzivatel.globalUser;
        
        String nazov = "moja";
        
        Kategoria expResult = new Kategoria(Kategoria.ineFinal, "ine", 0, 0, Uzivatel.globalUser, 0);
        expResult = new Kategoria(cislo, nazov, Kategoria.TELO, 1,pouzivatelID, 3);
        instance.pridajKategoriu(expResult, pouzivatelID);
        cislo = expResult.getCislo();
        Kategoria result = instance.vratKategoriu(nazov, pouzivatelID);
                
        assertEquals(expResult, result);

    }

    /**
     * Test of upravKategoriu method, of class PametovyKategoriaDao.
     */
    @Test
    public void testUpravKategoriu() {
             System.out.println("upravKategoriu");
        String nazov = "TutoChcemUpravit";
        Long cislo  = 2L;
        Kategoria upravovanaKategoria = new Kategoria(cislo, nazov, -1, -1, aktualneID, 10);
        
        instance.pridajKategoriu(upravovanaKategoria, aktualneID);
        
        Kategoria vratKategoriu = instance.vratKategoriu(cislo, aktualneID);
        
        Assert.assertEquals(cislo, vratKategoriu.getCislo());
        
        vratKategoriu.setNazov("upravenaKategoria");
        
        instance.upravKategoriu(vratKategoriu, aktualneID);
      
        String vratNazov = instance.vratNazov(cislo, aktualneID);
         
         assertEquals("upravenaKategoria",vratNazov );
    }
    
}
