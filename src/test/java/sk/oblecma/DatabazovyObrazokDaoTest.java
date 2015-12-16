/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
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
public class DatabazovyObrazokDaoTest {
    DatabazaSpojenie spojenie = new DatabazaSpojenie(true);
    ObrazokDao instance = new DatabazovyObrazokDao();
    
    public DatabazovyObrazokDaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        DatabazaSpojenie.jdbcTemplate.execute("delete from obrazok where id > 0");
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of pridajData method, of class DatabazovyObrazokDao.
     */
    @Test
    public void testPridajData() throws NepodariloSaPridatObrazokException {
        System.out.println("pridajData");
        File file = new File("obrazok.png");

        instance.pridajData(file);
        
    }

    /**
     * Test of zistiData method, of class DatabazovyObrazokDao.
     */
    @Test
    public void testZistiData() throws NepodariloSaPridatObrazokException {
        System.out.println("zistiData");
        List<Obrazok> expResult = new ArrayList<Obrazok>();
        
        String nazov = "file:/C:/Users/Slavomír/Documents/NetBeansProjects/OblecMa/obrazok.png";
        
        Obrazok obrazok = new Obrazok();
        obrazok.setSubor(nazov);
        expResult.add(obrazok);
        
        boolean ok = false;
        
        instance.pridajData(new File(nazov));
        
        List<Obrazok> result = instance.zistiData();
        
        for(Obrazok ob : result)
        {
            if(obrazok.getSubor().equals(ob.getSubor()))
            {
                ok = true;
            }            
        }
        
        assertEquals(true, ok);
       
    }
    
}
