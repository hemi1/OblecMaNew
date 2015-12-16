/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import com.sun.prism.Texture;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.ListModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alfred
 */
public class OblecMaTest {

    private OblecMa oblecMa;
    private KategoriaDao kategoriaDao;
    private UzivatelDao uzivatelDao;
    private Long idU;
    private OblecenieDao oblecenieDao;
    public OblecMaTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        uzivatelDao = new PametovyUzivatelDao();
        long uzivateId=1L;
        try {
            uzivatelDao.vytvorPouzivatela("a", "a", true);
            uzivatelDao.prihlasPouzivatela("a", "a");
            uzivateId = uzivatelDao.vratIdPrihlasenehoPouzivatela();
            idU=uzivateId;
        } catch (Exception ex) {
            Logger.getLogger(OblecMaTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        kategoriaDao = new PametovyKategoriaDao();
        kategoriaDao.pridajKategoriu(new Kategoria(1L, "prva", 1, 1, uzivateId, 10), uzivateId);
        kategoriaDao.pridajKategoriu(new Kategoria(2L, "druha", 2, 1, uzivateId, 10), uzivateId);
        OdporucenaZakazanaKombinaciaDao kombinacia = new PametovaOdporucanaZakazanaKombinaciaDao();
        oblecenieDao = new PametoveOblecenieDao();
        for (long i = 1; i < 20; i++) {
            Oblecenie oblecenie = new Oblecenie();
            if (i < 10) {
                oblecenie.setKategoria(1L);
            } else {
                oblecenie.setKategoria(2L);
            }
            oblecenie.setIdOblecenia(i);
            oblecenie.setNazov(""+i);
            oblecenieDao.pridajOblecenie(oblecenie, uzivateId);
        }
        PocasieDao pocasie = new DefaultPocasieDao();
        oblecMa = new OblecMa(pocasie, oblecenieDao, uzivatelDao, kategoriaDao,kombinacia);
        oblecMa.oznaceneMesto("kosice");
        try {
            oblecMa.aktualizujPocasie();
        } catch (NeuspesneZiskanieDatException ex) {
           
        }

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of formalnaPrilezitost method, of class OblecMa.
     */
    @Test
    public void testFormalnaPrilezitost() {
       System.out.println("formalnaPrilezitost");
  
        oblecMa.formalnaPrilezitost();
   
    }

    /**
     * Test of neformalnaPrilezitost method, of class OblecMa.
     */
    @Test
    public void testNeformalnaPrilezitost() {
        System.out.println("neformalnaPrilezitost");

        oblecMa.neformalnaPrilezitost();

   }

    /**
     * Test of vratKombinaciu method, of class OblecMa.
     */
    @Test
    public void testVratKombinaciu() {
        System.out.println("\n---------------------------------------------------------------------------\n");
        System.out.println("Pocet obleceni "+oblecenieDao.dajVsetkyOblecenia().size());
        System.out.println("Pocet obleceni pre uzivatela "+oblecenieDao.dajVsetkyOblecenia(idU).size());
        System.out.println("\n---------------------------------------------------------------------------\n");
        List<Oblecenie> komb = oblecMa.vratKombinaciu();
        
        assertEquals(2, komb.size());
    }

    /**
     * Test of oznaceneMesto method, of class OblecMa.
     */
    @Test
    public void testOznaceneMesto() {
        System.out.println("oznaceneMesto");
        String nastav = "kosice";
 
        oblecMa.oznaceneMesto(nastav);
   
    }

    /**
     * Test of aktualizujPocasie method, of class OblecMa.
     */
    @Test
    public void testAktualizujPocasie() throws Exception {
        System.out.println("aktualizujPocasie");
      
        oblecMa.aktualizujPocasie();
      
    }

    /**
     * Test of dajNasledujuceOdporucane method, of class OblecMa.
     */
    @Test
    public void testDajNasledujuceOdporucane() throws NeuspesneZiskanieDatException {
        System.out.println("dajNasledujuceOdporucane");
     
        List<Oblecenie> ziskaj = oblecMa.vratKombinaciu();
        
        JList list = new JList();
        list.setListData(ziskaj.toArray());
        
        ListModel model = list.getModel();
        
        oblecMa.odporucZakazKombinaciu(model, true, idU);
        
        Oblecenie zmen = ziskaj.get(1);
        
        
        List<Oblecenie> expResult = oblecMa.zmenJednoOblecenie(ziskaj, zmen);
        
        list.setListData(expResult.toArray());
        
        model = list.getModel();
        
        oblecMa.odporucZakazKombinaciu(model, true, idU);
        
        oblecMa.aktualizujPocasie();
        oblecMa.vycistPouziteKombinacie();
        
        oblecMa.vratKombinaciu();
        
        List<Oblecenie> result = oblecMa.dajNasledujuceOdporucane(idU);
        
        List<Long> expResultX = new ArrayList<>();
         List<Long> resultX = new ArrayList<>();
         
         for(Oblecenie obl : result)
         {
             resultX.add(obl.getIdOblecenia());
         }
         for(Oblecenie obl : expResult)
         {
             expResultX.add(obl.getIdOblecenia());
         }
         
         Collections.sort(resultX);
         Collections.sort(expResultX);
         
        assertEquals(expResultX, resultX);
    }

    /**
     * Test of odporucZakazKombinaciu method, of class OblecMa.
     */
    @Test
    public void testOdporucZakazKombinaciu() {
        System.out.println("odporucZakazKombinaciu");
        
        JList list = new JList();
        list.setListData(oblecMa.vratKombinaciu().toArray());
        
        ListModel model =list.getModel();
        
        boolean odporuc = false;
      
        oblecMa.odporucZakazKombinaciu(model, odporuc, idU);
       
    }

    /**
     * Test of porovnajPodobnostKategorii method, of class OblecMa.
     */
    @Test
    public void testPorovnajPodobnostKategorii() {
        System.out.println("porovnajPodobnostKategorii");
        Map<Oblecenie, Integer> kombinacia = new HashMap<>();
        
        for(Kategoria kat : kategoriaDao.vratZoznamKategorii(idU))
        {
            for(Oblecenie obl : oblecenieDao.dajObleceniePodlaKategorie(kat.getCislo(), idU))
            {
                kombinacia.put(obl, obl.getIdOblecenia().intValue());
            }
        }
        
        List<Integer> expResult = new ArrayList();
        for(Kategoria kat : kategoriaDao.vratZoznamKategorii(idU))
        {
            Oblecenie naj=null;
            for(Oblecenie obl : oblecenieDao.dajObleceniePodlaKategorie(kat.getCislo(), idU))
            {
               if(naj==null)
               {
                   naj=obl;
                   continue;
               }
               if(naj.getIdOblecenia().intValue()<obl.getIdOblecenia().intValue())
               {
                   naj=obl;
               }
               
            }
            if(naj!=null)
             expResult.add(naj.getIdOblecenia().intValue());
        }
        
        
        List<Oblecenie> resultX = OblecMa.porovnajPodobnostKategorii(kombinacia,kategoriaDao, idU);
        
        List<Integer> result = new ArrayList<>();
        
        for(Oblecenie ob : resultX)
        {
            result.add(ob.getIdOblecenia().intValue());
        }
        
        Collections.sort(result);
        Collections.sort(expResult);
        assertEquals(expResult, result);
    }

    /**
     * Test of bolaKombinacia method, of class OblecMa.
     */
    @Test
    public void testBolaKombinacia() {
        System.out.println("bolaKombinacia");
        JList list = new JList();
        list.setListData(oblecMa.vratKombinaciu().toArray());
        
        ListModel model =list.getModel();
        oblecMa.bolaKombinacia(model);
       
    }

    /**
     * Test of vycistPouziteKombinacie method, of class OblecMa.
     */
    @Test
    public void testVycistPouziteKombinacie() {
        System.out.println("vycistPouziteKombinacie");
    
        oblecMa.vycistPouziteKombinacie();
      
    }

    /**
     * Test of zmenJednoOblecenie method, of class OblecMa.
     */
    @Test
    public void testZmenJednoOblecenie() {
        System.out.println("zmenJednoOblecenie");

         List<Oblecenie> komb = oblecMa.vratKombinaciu();
        assertEquals(2, komb.size());
        Oblecenie zmenit = null;
        for(Oblecenie obl : komb){
            zmenit = obl;
            break;
        }
        List<Oblecenie> zmenenaKomb = oblecMa.zmenJednoOblecenie(komb,zmenit);
        
        for(Oblecenie obl : zmenenaKomb){
            if(obl.getIdOblecenia().equals(zmenit.getIdOblecenia()))
            {
                assertEquals(true, false);
                return;
            }
        }
        
        assertEquals(2, zmenenaKomb.size());
    }

    /**
     * Test of aplikujZakazaneKombinacie method, of class OblecMa.
     */
    @Test
    public void testAplikujZakazaneKombinacie() {
        System.out.println("aplikujZakazaneKombinacie");
      
        oblecMa.aplikujZakazaneKombinacie();
      
    }

}
