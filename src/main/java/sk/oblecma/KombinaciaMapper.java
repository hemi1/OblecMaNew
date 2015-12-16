/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Slavomír
 */
public class KombinaciaMapper implements RowMapper<Kombinacia>{

    private OblecenieDao mapovanie = new DefaultOblecenieDao();
    
    @Override
    public Kombinacia mapRow(ResultSet rs, int i) throws SQLException {
       
        
        List<Oblecenie>  oblecenieList = new ArrayList<Oblecenie>();
        
        for(int a=1;a<24;a++)
        {
            Long idOblecenia = null;
            String stlpec = "o"+a;
            
            idOblecenia = rs.getLong(stlpec);
            
            Oblecenie oblecenie = mapovanie.dajOblecenie(idOblecenia);
            if(oblecenie!=null)
            {
                oblecenieList.add(oblecenie);
            }
        }
        
         Kombinacia kombinacia = new Kombinacia(oblecenieList);
         kombinacia.nastavId(rs.getLong("id"));
        return kombinacia;
    }
    
}
