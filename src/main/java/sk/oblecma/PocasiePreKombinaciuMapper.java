/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author Slavomír
 */
public class PocasiePreKombinaciuMapper implements RowMapper<Pocasie>{

    public PocasiePreKombinaciuMapper() {
    }

    @Override
    public Pocasie mapRow(ResultSet rs, int i) throws SQLException {
        Pocasie pocasie = new Pocasie();
        
        pocasie.setTeplotaCezDen(rs.getInt("teplotaCezDen"));
        pocasie.setRychlostVetra(rs.getInt("rychlostVetra"));
        pocasie.setPravdepodobnostZrazok(rs.getInt("pravdepodobnostZrazok"));
        
        
        
        pocasie.setBurlivyVietor(rs.getBoolean("burlivyVietor"));
        pocasie.setChladno(rs.getBoolean("chladno"));
        pocasie.setDazd(rs.getBoolean("dazd"));
        pocasie.setHoruco(rs.getBoolean("horuco"));
        pocasie.setPriemerne(rs.getBoolean("priemerne"));
        pocasie.setSilnyVietor(rs.getBoolean("silnyvietor"));
        pocasie.setSlabyVietor(rs.getBoolean("slabyvietor"));
        pocasie.setSneh(rs.getBoolean("sneh"));
        
        pocasie.setTeplejsie(rs.getBoolean("teplejsie"));
        pocasie.setTeplo(rs.getBoolean("teplo"));
        pocasie.setTropickaHorucava(rs.getBoolean("tropickahorucava"));
        
        pocasie.setVelkaZima(rs.getBoolean("velkazima"));
        pocasie.setVelmiSilnyVietor(rs.getBoolean("silnyvietor"));
        pocasie.setZima(rs.getBoolean("zima"));
        
        
        return pocasie;
        
    }
    
}
