/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class DatabazovaHistoriaDaoTest {
    DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
    DatabazovaHistoriaDao historia = new DatabazovaHistoriaDao();
    private Long idUzivatela=Uzivatel.globalUser;
    private Map<Long, List<Kombinacia>> expResultMapa = new HashMap<>();
    private Kombinacia kombinaciaRovnaka = new Kombinacia();
    
    
    private OblecenieDao oblecenieDao;
    public DatabazovaHistoriaDaoTest() {
      
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
     //   DatabazaSpojenie.jdbcTemplate.execute("delete from kombinaciahistoria where id>0");
    }
    
    @Before
    public void setUp() {
          oblecenieDao = new PametoveOblecenieDao();
        expResultMapa.clear();
        for (long i = 1; i < 200; i++) {
            oblecenieDao.pridajOblecenie(new Oblecenie(), i);
        }
        historia = new DatabazovaHistoriaDao();
        if(historia==null)
        {
            System.out.println("CHYBA");
        }
        long i = 1;
        while (i < 100) {
            Long[] poleId = new Long[24];
            for (int j = 0; j < 5; j++) {
                poleId[j] = i;
                i++;
            }
            Kombinacia komb = Kombinacia.vratKombinaciuPodlaId(poleId, this.oblecenieDao);
            komb.nastavId(i);
            historia.pridaj(komb);
            kombinaciaRovnaka=komb;
            if(expResultMapa.containsKey(idUzivatela))
            {
                List<Kombinacia> get = expResultMapa.get(idUzivatela);
                get.add(komb);
                expResultMapa.put(idUzivatela,get);
            }
            else
            {
                List<Kombinacia> get = new ArrayList<>();
                get.add(komb);
                expResultMapa.put(idUzivatela,get);
            }
        }
        //System.out.println(historia.vratCeluHistoriu().size());
    }
    
    @After
    public void tearDown() {
        DatabazaSpojenie.jdbcTemplate.execute("delete from kombinaciahistoria where id>0");
    }

    /**
     * Test of nastavUzivatela method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testNastavUzivatela() {
        System.out.println("nastavUzivatela");
        Long id = Uzivatel.globalUser;
       
        historia.nastavUzivatela(id);

    }

    /**
     * Test of vratCeluHistoriu method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testVratCeluHistoriu() {
       assertEquals(20, historia.vratCeluHistoriu().size());
    }

    /**
     * Test of vratHistoriuOdoDna method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testVratHistoriuOdoDna() {
        Kombinacia spredDnaKomb;
        spredDnaKomb = new Kombinacia(new ArrayList<Oblecenie>(), new Date(111, 4, 4).toLocalDate());
        historia.pridaj(spredDnaKomb);
        
        System.out.println(new Date(112, 4, 4).toLocalDate());
        System.out.println(new Date(111, 4, 4).toLocalDate());
        
        assertEquals(21, historia.vratCeluHistoriu().size());
        assertEquals(20, historia.vratHistoriuOdoDna( new Date(112, 4, 4).toLocalDate()).size());
        assertEquals(21, historia.vratHistoriuOdoDna( new Date(111, 4, 4).toLocalDate()).size());
    }

    /**
     * Test of vratKombinacieZoDna method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testVratKombinacieZoDna() {
       Kombinacia spredDnaKomb;
        spredDnaKomb = new Kombinacia(new ArrayList<Oblecenie>(), new Date(111, 4, 4).toLocalDate());
        historia.pridaj(spredDnaKomb);

        assertEquals(1, historia.vratKombinacieZoDna(new Date(111, 4, 4).toLocalDate()).size());
        Kombinacia spredDnaKomb2;
        spredDnaKomb2 = new Kombinacia(new ArrayList<Oblecenie>(),new Date(111, 4, 4).toLocalDate());

        historia.pridaj(spredDnaKomb2);

        assertEquals(2, historia.vratKombinacieZoDna(new Date(111, 4, 4).toLocalDate()).size());
    }

    /**
     * Test of vymazHistoriu method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testVymazHistoriu() {
       historia.vymazHistoriu();
        assertEquals(0, historia.vratCeluHistoriu().size());
    }

    /**
     * Test of nastavDlzkuHistorie method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testNastavDlzkuHistorie() {
       
        /*???*/
    }

    /**
     * Test of pridaj method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testPridaj_Kombinacia() {
         Kombinacia novaKomb = new Kombinacia(new ArrayList<Oblecenie>());
        historia.pridaj(novaKomb);
        assertEquals(21, historia.vratCeluHistoriu().size());
    }

    /**
     * Test of pridaj method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testPridaj_List() {
        List<Kombinacia> pridavaneKombinacie = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Kombinacia novaKomb = new Kombinacia(new ArrayList<Oblecenie>());
            pridavaneKombinacie.add(novaKomb);
        }

        historia.pridaj(pridavaneKombinacie);

        assertEquals(25, historia.vratCeluHistoriu().size());
    }

    /**
     * Test of odober method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testOdober_Kombinacia() {
        Date date = new Date(111, 1, 1);
       Kombinacia novaKomb = new Kombinacia(new ArrayList<Oblecenie>(),date.toLocalDate());
               System.out.println(historia.vratCeluHistoriu().size());
        historia.pridaj(novaKomb);
        novaKomb.nastavId(21L);
        System.out.println(historia.vratCeluHistoriu().size());
        assertEquals(21, historia.vratCeluHistoriu().size());

        historia.odober(novaKomb);
        System.out.println(historia.vratCeluHistoriu().size());
        assertEquals(20, historia.vratCeluHistoriu().size());
        
    }

    /**
     * Test of odober method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testOdober_List() {
 
        List<Kombinacia> kombinacieZoDna = historia.vratKombinacieZoDna(LocalDate.now());
        historia.odober(kombinacieZoDna);
        assertEquals(0, historia.vratCeluHistoriu().size());
    }

    /**
     * Test of maRovnaku method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testMaRovnaku() {
       System.out.println("maRovnaku");
   
        boolean expResult = true;
        boolean result = historia.maRovnaku(kombinaciaRovnaka);
        assertEquals(expResult, result);
    }

    /**
     * Test of vratCeluHistoriuMapu method, of class DatabazovaHistoriaDao.
     */
    @Test
    public void testVratCeluHistoriuMapu() {
         System.out.println("vratCeluHistoriuMapu");
     
        Map<Long, List<Kombinacia>> result = historia.vratCeluHistoriuMapu();
        
        for(Long ID : expResultMapa.keySet())
        {
            if(!result.containsKey(ID))
            {
                assertNotNull(null);
                return;
            }
            for(Kombinacia expKombinacia : expResultMapa.get(ID))
            {
                boolean jeTam = false;
                for(Kombinacia vratena : result.get(ID))
                {
                    if(jeTam)
                        continue;
                    if(vratena.jeRovnaka(expKombinacia))
                    {
                        jeTam = true;
                    }
                }
                if(!jeTam)
                {
                    
                    assertNotNull(null);
                    return;
                }
                
            }
        }
        
        
  //      assertEquals(expResultMapa, result);
    }
    
}
