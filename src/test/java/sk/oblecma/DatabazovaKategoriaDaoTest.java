/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Slavomír
 */
public class DatabazovaKategoriaDaoTest {
       DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
       private Long aktualneID = Uzivatel.globalUser;
       private DatabazovaKategoriaDao instance = new DatabazovaKategoriaDao();
       private Long idPridana = 1L;
       private String nazovPridana = "nazov";
       Kategoria kategoriaPridaj = new Kategoria(idPridana, nazovPridana, 0, 1,aktualneID, 10);
    public DatabazovaKategoriaDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        DatabazaSpojenie.jdbcTemplate.execute("delete from kategoria where cislo > 0");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of vratNazov method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratNazov() {
        System.out.println("vratNazov");
        Long cislo = Kategoria.ineFinal;

        String expResult = "ine";
        String result = instance.vratNazov(cislo,aktualneID);
       assertEquals(expResult, result);
     
   }

    /**
     * Test of vratCislo method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratCislo() {
        System.out.println("vratCislo");
        String nazov = "ine";

        Long expResult = Kategoria.ineFinal;
        Long result = instance.vratCislo(nazov,aktualneID);
        assertEquals(expResult, result);
    
    }

    /**
     * Test of vratPocet method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratPocet() {
        System.out.println("vratPocet");
   
        int expResult = 1;
        int result = instance.vratPocet(aktualneID);
        assertEquals(expResult, result);
       
    }

    /**
     * Test of pridajKategoriu method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testPridajKategoriu() {
        System.out.println("pridajKategoriu");
        
        instance.pridajKategoriu(kategoriaPridaj,aktualneID);
      
        Long vratCislo = instance.vratCislo(nazovPridana, aktualneID);
       Assert.assertEquals(idPridana, vratCislo);
    }

    /**
     * Test of vratZoznamKategorii method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratZoznamKategorii() {
        System.out.println("vratZoznamKategorii");
      
        List<Kategoria> result = instance.vratZoznamKategorii(aktualneID);
        
        for(Kategoria kat : result)
        {
            if(aktualneID.equals(kat.getPouzivatelID())==false)
            {
                assertEquals(aktualneID, kat.getPouzivatelID());
                return;
            }
        }
        
        
        assertNotNull(result);   
    }

    /**
     * Test of odstranKategoriu method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testOdstranKategoriu() {
        System.out.println("odstranKategoriu");
        String nazovKategorie = "odstran";
        instance.pridajKategoriu(new Kategoria(2L, nazovKategorie, 0, 0, aktualneID, 0), aktualneID);
        instance.odstranKategoriu(nazovKategorie,aktualneID);
        
        Long vratCislo = instance.vratCislo(nazovKategorie, aktualneID);
        assertEquals(Kategoria.ineFinal, vratCislo);
    }

    /**
     * Test of vratKategoriu method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratKategoriu_Long_Long() {
       System.out.println("vratKategoriu(Long id)");

        Kategoria result = instance.vratKategoriu(idPridana,aktualneID);
        
        assertEquals(kategoriaPridaj.getCislo(), result.getCislo());
    }

    /**
     * Test of vratKategoriu method, of class DatabazovaKategoriaDao.
     */
    @Test
    public void testVratKategoriu_String_Long() {
       System.out.println("vratKategoriu(String nazov)");

        Kategoria result = instance.vratKategoriu(nazovPridana,aktualneID);
        
        assertEquals(kategoriaPridaj.getCislo(), result.getCislo());
    }

    /**
     * Test of upravKategoriu method, of class DatabazovaKategoriaDao.
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
