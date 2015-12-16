/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Slavomír
 */
public class DatabazovaOdporucanaZakazanaKombinaciaDaoTest {
    
    private DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
    private PocasieDao pocasie = new DefaultPocasieDao();
    private DatabazovaOdporucanaZakazanaKombinaciaDao instance = new DatabazovaOdporucanaZakazanaKombinaciaDao();
    private Long idUzivatela=Uzivatel.globalUser;
    
    private Pocasie podmienkyPocasia = pocasie.vratPocasie();
    
    private Long poleOdporucena[] = 
        {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L,13L,14L,15L,16L,17L,18L,19L,20L,1L,22L,23L,24L};
    
    private Long poleZakazana[] =
        {1L,2L,3L,4L,5L,6L,7L,8L,9L,10L,11L,12L,13L,14L,15L,16L,17L,18L,19L,20L,1L,22L,44L,null};    
            
    public DatabazovaOdporucanaZakazanaKombinaciaDaoTest() {
        try {
            pocasie.zistiPocasieData("kosice");
            pocasie.vyhodnotPocasie();
        } catch (NeuspesneZiskanieDatException ex) {
            System.out.println("Chyba internetu nastavi sa to rucne na >> \n dazd, priemernu teplotu a slaby vietor \n (ciselne a percentualne udaje nebudu odpovedat nastaveniam)");
            podmienkyPocasia.setDazd(true);
            podmienkyPocasia.setPriemerne(true);
            podmienkyPocasia.setSlabyVietor(true);
        }
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        DatabazaSpojenie.jdbcTemplate.execute("delete from kombinacia where id > 0");
        DatabazaSpojenie.jdbcTemplate.execute("delete from zoznamPodmienok where idPodmienka > 0");
        
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of OdporucKombinaciu method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testOdporucKombinaciu() {
        System.out.println("OdporucKombinaciu");
        KombinaciaANG odporucenaKombinacia = new KombinaciaANG();       
        
        odporucenaKombinacia.setKombinacia(poleOdporucena);
     
        
        boolean formalnost = false;

        
        instance.OdporucKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost,idUzivatela);
        
    }

    /**
     * Test of vratOdporuceneKombinacie method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testVratOdporuceneKombinacie() {
        System.out.println("vratOdporuceneKombinacie");
 
        boolean formalnost = false;


        List<KombinaciaANG> result = instance.vratOdporuceneKombinacie(podmienkyPocasia, formalnost,idUzivatela);
        
        boolean ok =true;
        
        for(KombinaciaANG aktualna  : result)
        {
            if(Arrays.equals(poleOdporucena, aktualna.getKombinacia()))
            {
                org.junit.Assert.assertArrayEquals(poleOdporucena, aktualna.getKombinacia());
            }
            else
            {
                ok =false;
            }
        }
        
       
       
            Assert.assertEquals(true, ok);
       
      
    }

    /**
     * Test of ZakazKombinaciu method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testZakazKombinaciu() {
        System.out.println("ZakazKombinaciu");
          KombinaciaANG zakazanaKombinacia = new KombinaciaANG();
            
        zakazanaKombinacia.setKombinacia(poleZakazana);
     

        boolean formalnost = false;
        
        instance.ZakazKombinaciu(zakazanaKombinacia, podmienkyPocasia, formalnost,idUzivatela);
        
    }

    /**
     * Test of vratZakazaneKombinacie method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testVratZakazaneKombinacie() {
        System.out.println("vratZakazaneKombinacie");
      
        boolean formalnost = false;


        List<KombinaciaANG> result = instance.vratZakazaneKombinacie(podmienkyPocasia, formalnost,idUzivatela);
        
        boolean ok =true;
        
        for(KombinaciaANG aktualna  : result)
        {
            if(Arrays.equals(poleZakazana, aktualna.getKombinacia()))
            {
                org.junit.Assert.assertArrayEquals(poleZakazana, aktualna.getKombinacia());
            }
            else
            {
                ok =false;
            }
        }
        
       
       
            Assert.assertEquals(true, ok);
       
    }

    /**
     * Test of vratVsetkyOdporuceneKombinacie method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testVratVsetkyOdporuceneKombinacie() {
        System.out.println("vratVsetkyOdporuceneKombinacie");

        boolean formalnost = false;


        List<KombinaciaANG> result = instance.vratVsetkyOdporuceneKombinacie(idUzivatela);
        
        boolean ok =true;
        
        for(KombinaciaANG aktualna  : result)
        {
            if(Arrays.equals(poleOdporucena, aktualna.getKombinacia()))
            {
                org.junit.Assert.assertArrayEquals(poleOdporucena, aktualna.getKombinacia());
            }
            else
            {
                ok =false;
            }
        }
        
       
       
            Assert.assertEquals(true, ok);
    }

    /**
     * Test of vratVsetkyZakazaneKombinacie method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testVratVsetkyZakazaneKombinacie() {
        System.out.println("vratVsetkyZakazaneKombinacie");
 
        boolean formalnost = false;


        List<KombinaciaANG> result = instance.vratVsetkyZakazaneKombinacie(idUzivatela);
        
        boolean ok =true;
        
        for(KombinaciaANG aktualna  : result)
        {
            if(Arrays.equals(poleZakazana, aktualna.getKombinacia()))
            {
                org.junit.Assert.assertArrayEquals(poleZakazana, aktualna.getKombinacia());
            }
            else
            {
                ok =false;
            }
        }
        
       
       
            Assert.assertEquals(true, ok);
    }

    /**
     * Test of presunDoZakazanej method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testPresunDoZakazanej() {
        System.out.println("presunDoZakazanej");
        Long idKombinacia = 1L;

        instance.presunDoZakazanej(idKombinacia);
        List<KombinaciaANG> vratVsetkyOdporucaneKombinacie = instance.vratVsetkyOdporuceneKombinacie(idUzivatela);
        
        for(KombinaciaANG komb : vratVsetkyOdporucaneKombinacie)
        {
            if(idKombinacia.equals(komb.getId()))
            {
                Assert.assertEquals(false, true);
                return;
            }
        }
        
        Assert.assertEquals(false, false);
    }

    /**
     * Test of presunDoOdporucanej method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testPresunDoOdporucanej() {
        System.out.println("presunDoOdporucanej");
         Long idKombinacia = 2L;

        instance.presunDoOdporucanej(idKombinacia);
        List<KombinaciaANG> vratVsetkyZakazaneKombinacie = instance.vratVsetkyOdporuceneKombinacie(idUzivatela);
        
        for(KombinaciaANG komb : vratVsetkyZakazaneKombinacie)
        {
            if(idKombinacia.equals(komb.getId()))
            {
                Assert.assertEquals(false, true);
                return;
            }
        }
        
        Assert.assertEquals(false, false);
    }

    /**
     * Test of odstranOdporucanu method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testOdstranOdporucanu() {
        System.out.println("odstranOdporucanu");
        Long idKombinacia = 1L;

        instance.odstranOdporucanu(idKombinacia);
        List<KombinaciaANG> vratVsetkyOdporuceneKombinacie = instance.vratVsetkyOdporuceneKombinacie(idUzivatela);
         for(KombinaciaANG komb : vratVsetkyOdporuceneKombinacie)
        {
            if(idKombinacia.equals(komb.getId()))
            {
                Assert.assertEquals(false, true);
                return;
            }
        }
        
        Assert.assertEquals(false, false);
    }

    /**
     * Test of odstranZakazanu method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testOdstranZakazanu() {
        System.out.println("odstranZakazanu");
        Long idKombinacia = 1L;

        instance.odstranOdporucanu(idKombinacia);
        List<KombinaciaANG> vratVsetkyZakazaneKombinacie = instance.vratVsetkyZakazaneKombinacie(idUzivatela);
         for(KombinaciaANG komb : vratVsetkyZakazaneKombinacie)
        {
            if(idKombinacia.equals(komb.getId()))
            {
                Assert.assertEquals(false, true);
                return;
            }
        }
        
        Assert.assertEquals(false, false);
    }

    /**
     * Test of vratPocasiePreKombinaciu method, of class DatabazovaOdporucanaZakazanaKombinaciaDao.
     */
    @Test
    public void testVratPocasiePreKombinaciu() {
        System.out.println("vratPocasiePreKombinaciu");
        Long idKombinacia = 1L;
      
        Pocasie result = instance.vratPocasiePreKombinaciu(idKombinacia);
        org.junit.Assert.assertNotNull(result);
    }
    
}
