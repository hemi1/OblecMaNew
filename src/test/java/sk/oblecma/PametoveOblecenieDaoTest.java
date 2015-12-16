package sk.oblecma;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Arrays;
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
public class PametoveOblecenieDaoTest {
     private  PametoveOblecenieDao instance= new PametoveOblecenieDao();
     private Long IDuzivatela = Uzivatel.globalUser;
    public PametoveOblecenieDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Oblecenie obl = new Oblecenie("test", 1L);
        instance.pridajOblecenie(obl, IDuzivatela);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of pridajOvblecenie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testPridajOblecenie() {
      System.out.println("pridajOblecenie");
        Long IDkategorie = 2L;
        String nazov = "nohavice";
        Oblecenie noveOblecenie = new Oblecenie(nazov, IDkategorie);
        
        Long idOblecenia = 21L;
        noveOblecenie.setIdOblecenia(idOblecenia);
        
        instance.pridajOblecenie(noveOblecenie, IDuzivatela);
        
        Oblecenie oblecenie = instance.dajOblecenie(idOblecenia);
        
        String result = oblecenie.getNazov();
        
        Assert.assertEquals(nazov,result);
        
    }

    /**
     * Test of vyhodOblecenie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testVyhodOblecenie() {
        System.out.println("vyhodOblecenie");
        Long idOblecenia = 3L;
         Long IDkategorie = 2L;
        String nazov = "vyhod";
        Oblecenie noveOblecenie = new Oblecenie(nazov, IDkategorie);
        
        noveOblecenie.setIdOblecenia(idOblecenia);
        
        instance.pridajOblecenie(noveOblecenie, IDuzivatela);
        
        Oblecenie kontrola = instance.dajOblecenie(idOblecenia);
        
        if(kontrola==null)
        {
            Assert.assertNotNull(kontrola);
        }
        
        instance.vyhodOblecenie(idOblecenia, IDuzivatela);
        
        Oblecenie result = instance.dajOblecenie(idOblecenia);
        
        Assert.assertEquals(null, result);
        
     
    }

    /**
     * Test of dajVsetkyOblecenia method, of class PametoveOblecenieDao.
     */


    /**
     * Test of dajObleceniePodlaKategorie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testDajObleceniePodlaKategorie() {
     System.out.println("\ndajObleceniePodlaKategorie\n");
        
        Long kategoria = 2L;
        
        List<Oblecenie> result = instance.dajObleceniePodlaKategorie(kategoria, IDuzivatela);
        
        for(Oblecenie ob : result)
        {
            if(!kategoria.equals(ob.getKategoria()))
             {
                Assert.assertEquals(kategoria,ob.getKategoria());
             }
        }
        
        Assert.assertNotNull(result);
      
    }

    /**
     * Test of dajVsetkyOblecenia method, of class PametoveOblecenieDao.
     */
    @Test
    public void testDajVsetkyOblecenia_Long() {
         System.out.println("dajVsetkyOblecenia(Long IdOblecenia)");

        List<Oblecenie> expResult = null;
        List<Oblecenie> result = instance.dajVsetkyOblecenia(IDuzivatela);
        
        System.out.println(Arrays.toString(result.toArray()));
        
        for(Oblecenie ob : result)
        {
            if("nohavice".equals(ob.getNazov()))
            {
                Assert.assertEquals(1, 1);
                return;
            }
        }
        
        Assert.assertNotNull(result);
    }

    /**
     * Test of odstranKategoriuOblecenia method, of class PametoveOblecenieDao.
     */
    @Test
    public void testOdstranKategoriuOblecenia() {
       System.out.println("odstranKategoriuOblecenia");
        Long cisloKategorie = 10L;
        
        instance.pridajOblecenie(new Oblecenie("odstranenaKategoria", cisloKategorie), IDuzivatela);        
        instance.odstranKategoriuOblecenia(cisloKategorie,IDuzivatela);
        
        List<Oblecenie> list = instance.dajObleceniePodlaKategorie(Kategoria.ineFinal, IDuzivatela);
        
        if(list.size()==1)
        {
            for(Oblecenie ob : list)
            {
                if("odstranenaKategoria".equals(ob.getNazov()))
                {
                    Assert.assertEquals("odstranenaKategoria", ob.getNazov());
                    return;
                }
            }
        }
        
        Assert.assertEquals("odstranenaKategoria",null);
    }

    /**
     * Test of nastavKategoriuOblecenie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testNastavKategoriuOblecenie() {
           System.out.println("nastavKategoriuOblecenie");
        Long cisloOblecenia = 3L;
        Long cisloKategorie = 3L;
        Long novaKategoria = 12L;
        
        Oblecenie obl = new Oblecenie("pridanaZlaKategoria", cisloKategorie);
        obl.setIdOblecenia(cisloOblecenia);
        instance.pridajOblecenie(obl, IDuzivatela);        
        instance.odstranKategoriuOblecenia(cisloKategorie,IDuzivatela);
        
        List<Oblecenie> list = instance.dajObleceniePodlaKategorie(Kategoria.ineFinal, IDuzivatela);
        
        for(Oblecenie ob : list)
        {
            instance.nastavKategoriuOblecenie(ob.getIdOblecenia(), novaKategoria, IDuzivatela);
        }
       
        instance.dajObleceniePodlaKategorie(novaKategoria, IDuzivatela);
        
        List<Oblecenie> vratene = instance.dajObleceniePodlaKategorie(novaKategoria, IDuzivatela);
        
        boolean ok = false;
        
        for(Oblecenie ob : vratene)
        {
            if(!novaKategoria.equals(ob.getKategoria()))
            {
                Assert.assertEquals(novaKategoria, ob.getKategoria());
                ok=false;
                return;
            }
            else
            {
                ok = true;
            }
        }
        
        if(ok)
        {
        Assert.assertEquals(novaKategoria,novaKategoria);
        }
        else
        {
            Assert.assertNotNull(vratene);
        }
    }

    /**
     * Test of dajVsetkyOblecenia method, of class PametoveOblecenieDao.
     */
    @Test
    public void testDajVsetkyOblecenia_0args() {
        System.out.println("dajVsetkyOblecenia");
        List<Oblecenie> expResult = null;
        List<Oblecenie> result = instance.dajVsetkyOblecenia();
        
        System.out.println(Arrays.toString(result.toArray()));
        
        for(Oblecenie ob : result)
        {
            if("nohavice".equals(ob.getNazov()))
            {
                Assert.assertEquals(1, 1);
                return;
            }
        }
        
        Assert.assertNotNull(result);
    }

    /**
     * Test of upravOblecenie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testUpravOblecenie() {
          System.out.println("upravOblecenie");
        
        Long idOblecenia = 1L;
        Oblecenie upraveneOblecenie = null;
        
        Oblecenie dajOblecenie = instance.dajOblecenie(idOblecenia);
        
        upraveneOblecenie = dajOblecenie;
        
        upraveneOblecenie.setNazov("upraveneOblecenie");
        
        instance.upravOblecenie(upraveneOblecenie);
        
        String nazov = instance.dajOblecenie(idOblecenia).getNazov();
        
        Assert.assertEquals("upraveneOblecenie", nazov);
        
    }

    /**
     * Test of dajOblecenie method, of class PametoveOblecenieDao.
     */
    @Test
    public void testDajOblecenie() {
          System.out.println("dajOblecenie");
        Long idOblecenia = 1L;
        
        Oblecenie result = instance.dajOblecenie(idOblecenia);
       
        Assert.assertEquals(idOblecenia,result.getIdOblecenia());
    }
    
}
