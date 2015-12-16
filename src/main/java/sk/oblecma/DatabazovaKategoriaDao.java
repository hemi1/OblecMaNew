/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotove testy
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 *
 * @author Slavomír
 */
public class DatabazovaKategoriaDao implements KategoriaDao{
     private BeanPropertyRowMapper rowMapper;
    
     public DatabazovaKategoriaDao() {
     rowMapper= new  BeanPropertyRowMapper();
     rowMapper.setMappedClass(Kategoria.class);
    }
    
    
    
    @Override
    public String vratNazov(Long cislo,Long pouzivatelID) {
         String vratSql = "select nazov from kategoria where cislo = ? and (pouzivatelID = ? or pouzivatelID = ?)";
        try{
       return  DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,String.class,cislo,pouzivatelID,Uzivatel.globalUser);
        }
        catch(DataAccessException e)
        {
            return "bola chyba";
        }
    }

    @Override
    public Long vratCislo(String nazov,Long pouzivatelID) {
         String vratSql = "select cislo from kategoria where nazov = ? and pouzivatelID = ?";
        try{
       return  DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,Long.class,nazov,pouzivatelID);
        }
        catch(DataAccessException e)
        {
            return Kategoria.ineFinal;
        }
    }

    @Override
    public int vratPocet(Long pouzivatelID) {
        String pocetSql = "select count(*) from kategoria where pouzivatelID = ? or pouzivatelID = ?";
        return DatabazaSpojenie.jdbcTemplate.queryForObject(pocetSql,Integer.class,pouzivatelID,Uzivatel.globalUser);
    }

    @Override
    public void pridajKategoriu(Kategoria kategoria,Long pouzivatelID) {
        String pridajSql = "insert into kategoria values(null, ? , ? , ? , ? , ? )";
        try{
        DatabazaSpojenie.jdbcTemplate.update(pridajSql,kategoria.getNazov(),kategoria.getMaximalnyPocetNoseniBezPrania(),kategoria.getLokacia(),kategoria.getVrstva(),kategoria.getPouzivatelID());
        }
        catch(DataAccessException e)
        {
            
        }
    }

    @Override
    public List<Kategoria> vratZoznamKategorii(Long pouzivatelID) {
        String vratZoznam = "select * from kategoria where pouzivatelID = ? or pouzivatelID = ?";
        try{
        return DatabazaSpojenie.jdbcTemplate.query(vratZoznam, rowMapper,pouzivatelID,Uzivatel.globalUser);
        }
        catch(DataAccessException e)
        {
            return new ArrayList<Kategoria>();
        }
    }

    @Override
    public void odstranKategoriu(String nazovKategorie,Long pouzivatelID) {
        String odstranSql = "delete from kategoria where nazov = ? and cislo > 0 and pouzivatelID = ?";
        DatabazaSpojenie.jdbcTemplate.update(odstranSql,nazovKategorie,pouzivatelID);
    }

    @Override
    public Kategoria vratKategoriu(Long cislo,Long pouzivatelID) {
        String vratSql = "select * from kategoria where cislo = ? and (pouzivatelID = ? or pouzivatelID = ?)";
        try{
       return (Kategoria) DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,rowMapper,cislo,pouzivatelID,Uzivatel.globalUser);
        }
        catch(DataAccessException e)
        {
            return (Kategoria) DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,rowMapper,Kategoria.ineFinal,Uzivatel.globalUser,Uzivatel.globalUser);
        }
    }

    @Override
    public Kategoria vratKategoriu(String nazovKategorie,Long pouzivatelID) {
        String vratSql = "select * from kategoria where nazov = ? and (pouzivatelID = ? or pouzivatelID = ?)";
        try{
       return (Kategoria) DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,rowMapper,nazovKategorie,pouzivatelID,Uzivatel.globalUser);
        }
        catch(DataAccessException e)
        {
            return (Kategoria) DatabazaSpojenie.jdbcTemplate.queryForObject(vratSql,rowMapper,"ine",Uzivatel.globalUser,Uzivatel.globalUser);
        }
    }

    @Override
    public void upravKategoriu(Kategoria upravovanaKategoria, Long aktualneID) {
      String upravSql = "update kategoria set nazov = ? ,lokacia = ? ,vrstva = ? where pouzivatelID = ? and cislo = ?";
      DatabazaSpojenie.jdbcTemplate.update(upravSql,upravovanaKategoria.getNazov(),upravovanaKategoria.getLokacia(),
              upravovanaKategoria.getVrstva(),upravovanaKategoria.getPouzivatelID(),upravovanaKategoria.getCislo());
    }
    
}
