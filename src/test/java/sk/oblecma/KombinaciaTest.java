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

/**
 *
 * @author alfred
 */
public class KombinaciaTest {
    private OblecenieDao zoznamOblecenia;

    public KombinaciaTest() {
        zoznamOblecenia = new PametoveOblecenieDao();
        for (int i = 0; i < 10; i++) {
            Oblecenie oblecenie = new Oblecenie();
            oblecenie.setIdOblecenia(i);

            zoznamOblecenia.pridajOblecenie(oblecenie, 1L);
        }
        
        
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
     * Test of vratKOmbinaciuPodlaIdTest method, of class Kategoria.
     */
    @Test
    public void vratKombinaciuPodlaIdTest() {
        
        Kombinacia komb = new Kombinacia(this.zoznamOblecenia.dajVsetkyOblecenia());
        
        Kombinacia vysledok = Kombinacia.vratKombinaciuPodlaId(komb.vratZoznamId(), zoznamOblecenia);
        
        Assert.assertEquals(true, vysledok.jeRovnaka(komb));
    }
}
