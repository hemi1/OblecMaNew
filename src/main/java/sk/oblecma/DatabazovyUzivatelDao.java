package sk.oblecma;

// testy hotove


import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public class DatabazovyUzivatelDao implements UzivatelDao{
    
    private List<Uzivatel> zoznamUzivatelov;
    
    private Uzivatel prihlasenyUzivatel;
    private BeanPropertyRowMapper rowMapper;
    
    
    public DatabazovyUzivatelDao() {
    zoznamUzivatelov = new ArrayList<>();
    prihlasenyUzivatel = null;
    rowMapper= new  BeanPropertyRowMapper();
    rowMapper.setMappedClass(Uzivatel.class);
    }
       
    private boolean vratPohlavie(Long id) {
        String selectSql ="select muz from pouzivatel where id= ?";
                
        try{
        return DatabazaSpojenie.jdbcTemplate.queryForObject(selectSql, Boolean.class,id);
        }
        catch(DataAccessException e)
        {
            return false;
        }
    }
    @Override
    public void vytvorPouzivatela(String meno, String heslo, boolean muz) throws Exception {
        
        String selectSql ="select meno from pouzivatel where meno= ?";
        try{
        String existuje = DatabazaSpojenie.jdbcTemplate.queryForObject(selectSql,String.class,meno);
        if(existuje!=null)
        {
            throw new UzivatelExistujeException();
        }
        }
        catch(DataAccessException e)
        {
            System.out.println("Nepodarilo sa ziskat ci tam je taky uzivatel");
        }
        String insertSql = "insert into pouzivatel values( null , ? , ?, ? )";
        try{
        DatabazaSpojenie.jdbcTemplate.update(insertSql,meno,heslo,muz);
        }
        catch(DataAccessException e)
        {
            System.out.println("Nepodarilo sa pridat uzivatela do databazy");
        }
            
        
    }
    @Override
    public void prihlasPouzivatela(String meno, String heslo) throws NieJePrihlasenyZiadenPouzivatelException, JePrihlasenyInyUzivatelException {
        
        String sql = "select * from pouzivatel where meno= ? and heslo= ? and id is not null";
        
        if(prihlasenyUzivatel!=null)
        {
            throw new JePrihlasenyInyUzivatelException();
        }
        
        try{
        Uzivatel queryForObject =(Uzivatel) DatabazaSpojenie.jdbcTemplate.queryForObject(sql,rowMapper,meno,heslo);
        
        
        if(!meno.equals(queryForObject.getMeno()))
        {
            throw new NieJePrihlasenyZiadenPouzivatelException();
        }
        if(!heslo.equals(queryForObject.getHeslo()))
        {
            throw new NieJePrihlasenyZiadenPouzivatelException();
        }
        prihlasenyUzivatel = queryForObject;
        }
        catch(DataAccessException e)
        {
            throw new NieJePrihlasenyZiadenPouzivatelException();
        }
        if(prihlasenyUzivatel==null)
       {
           throw new NieJePrihlasenyZiadenPouzivatelException();
       }
    }

    @Override
    public void odhlasPouzivatela() throws Exception{
       if(prihlasenyUzivatel==null)
          throw new NieJePrihlasenyZiadenPouzivatelException();
       prihlasenyUzivatel=null;
     }

    @Override
    public boolean jePrihlasny(String meno) {
      if(prihlasenyUzivatel==null)
          return false;
      
      return prihlasenyUzivatel.getMeno().equals(meno);
        
    }

    @Override
    public Long vratIdPrihlasenehoPouzivatela() throws NieJePrihlasenyZiadenPouzivatelException {
        if(prihlasenyUzivatel==null)
        {
            throw new NieJePrihlasenyZiadenPouzivatelException();
        }
        return prihlasenyUzivatel.getId();
    }

    @Override
    public List<Long> vratIdVsetkychUzivatelov() {
        String vratIdVsetkychSql = "Select id from pouzivatel";
        
        try{
        return DatabazaSpojenie.jdbcTemplate.query(vratIdVsetkychSql,new LongIdMapper());
        }
        catch(DataAccessException e)
        {
            
        }
        return new ArrayList<>();
    }

    @Override
    public boolean jeMuz(Long id) {
        return vratPohlavie(id);
    }

    @Override
    public boolean jeZena(Long id) {
        return !vratPohlavie(id);
    }

   


    
}
