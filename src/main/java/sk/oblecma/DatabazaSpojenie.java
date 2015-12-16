/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import org.springframework.jdbc.core.JdbcTemplate;



/**
 *
 * @author Slavomír
 */



public class DatabazaSpojenie {
        public static JdbcTemplate jdbcTemplate;

    public DatabazaSpojenie(boolean tests) {
         
        if(tests)
        {
            jdbcTemplate = JdbcTemplateFactory.INSTANCE.getJdbcTemplateTesting();
        }
        else
        {
            jdbcTemplate = JdbcTemplateFactory.INSTANCE.getJdbcTemplate();
        }
        if(jdbcTemplate==null)
        {
              System.out.println("nemam jdbc");
        }
       // jdbcTemplate.execute("use databse skuska");
        
    }
        
}
