/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.io.File;
import java.util.ArrayList;
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
public class PametovyObrazokDaoTest {
    
    PametovyObrazokDao instance = new PametovyObrazokDao();
    
    public PametovyObrazokDaoTest() {
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
     * Test of zistiData method, of class PametovyObrazokDao.
     */
    @Test
    public void testZistiData() {
        System.out.println("zistiData");
        File file[] = {
            new File("subor1.png"),
            new File("subor7.png"),
            new File("subor6.png"),
            new File("subor5.png"),
            new File("subor4.png"),
            new File("subor3.png"),
            new File("subor2.png"),
            new File("subor1.png")
            
        };
        
        int pocet_pridanych  = 8;
        int pocet = instance.zistiData().size();
        
        for(File fileX : file)
        {
        instance.pridajData(fileX);
        }
        List<Obrazok> zistiData = instance.zistiData();
        
       int novyPocet = zistiData.size();
       
       if(novyPocet!=pocet+pocet_pridanych)
       {
           assertEquals(true, false);
           return;
       }
        
       List<String> ziskane = new ArrayList<>();
       
        for(Obrazok ob : zistiData)
        {
            ziskane.add(ob.getFile().getAbsolutePath());
        }
        
        boolean ok = false;
        
        for(File fileX : file)
        {
            ok =true;
            String path = fileX.getAbsolutePath();
            if(!ziskane.contains(path))
            {
                ok=false;
                break;
            }
        }
        
        assertEquals(true, ok);
    }

    /**
     * Test of pridajData method, of class PametovyObrazokDao.
     */
    @Test
    public void testPridajData() {
        System.out.println("pridajData");
        File file = new File("testovaciaCesta.png");
        
        int pocet = instance.zistiData().size();
        
        instance.pridajData(file);
       
        List<Obrazok> zistiData = instance.zistiData();
        
       int novyPocet = zistiData.size();
       
       if(novyPocet!=pocet+1)
       {
           assertEquals(true, false);
           return;
       }
        
        for(Obrazok ob : zistiData)
        {
            System.out.printf(ob.getSubor());
            if(ob.getSubor().equals(file.getAbsolutePath()))
            {
                assertEquals(true, true);
                return;
            }
        }
        
        assertEquals(true, false);
    }
    
}
