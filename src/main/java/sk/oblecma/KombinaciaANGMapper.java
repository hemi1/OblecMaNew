/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Slavomír
 */
public class KombinaciaANGMapper implements RowMapper<KombinaciaANG>{

    @Override
    public KombinaciaANG mapRow(ResultSet rs, int i) throws SQLException {
        
        KombinaciaANG kombinacia = new KombinaciaANG(24);
        
    //    Long[] LongKombinacia = new Long[24];
        
        kombinacia.setId(rs.getLong("id"));
   //     System.out.println("id uzivatela = "+rs.getLong("idUzivatela"));
        for(int r=1;r<25;r++)
        {
            String nazov = "o"+r;
         //   LongKombinacia[r-1]=rs.getLong(nazov);
          // LongKombinacia[r-1]=0L;
            kombinacia.setKombinacia(r-1,rs.getLong(nazov));
        }
        
     //   kombinacia.setKombinacia(LongKombinacia);
        System.out.println(Arrays.toString(kombinacia.getKombinacia()));
        return kombinacia;
    }
    
}
