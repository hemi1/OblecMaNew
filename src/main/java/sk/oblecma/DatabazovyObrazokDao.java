package sk.oblecma;

// testy hotove

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


/**
 *
 * @author student
 */
public class DatabazovyObrazokDao implements ObrazokDao{
    private BeanPropertyRowMapper rowMapper;

    public DatabazovyObrazokDao()
    {
        rowMapper= new  BeanPropertyRowMapper();
        rowMapper.setMappedClass(Obrazok.class);
    }

    /**
     *
     * @param file
     * @throws sk.oblecma.NepodariloSaPridatObrazokException
     */
    @Override
    public void pridajData(File file) throws NepodariloSaPridatObrazokException
    {
        URI path = file.toURI();
        String sql = "insert into obrazok values(NULL,'"+path+"')";
        try {
             DatabazaSpojenie.jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
           throw new NepodariloSaPridatObrazokException();
        }
       
        
    }
    
    @Override
    public List<Obrazok> zistiData()
    {
     //   Obrazok obrazok_class = new Obrazok();

         String sqlVsetky = "select * from obrazok ";
         try{
             return DatabazaSpojenie.jdbcTemplate.query(sqlVsetky, rowMapper);
           
         }
         catch(DataAccessException e)
         {
             return new ArrayList<>();      
         }
         catch(InstantiationError e)
         {
             return new ArrayList<>();
         }
        
    }

    
    
  
    
    
}