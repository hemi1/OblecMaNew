package sk.oblecma;



import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.swing.JOptionPane;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
//import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Slavomír
 */
public enum JdbcTemplateFactory {
    INSTANCE;

    JdbcTemplateFactory() {
        if(spustac.NapojeneNaDatabazu==false)
        {
            JOptionPane.showMessageDialog(null, "Databaza bola vypnuta v zdrojovom kode alebo nastaveniach \n Aplikacia ide na pamet" ,"Upozornenie",JOptionPane.OK_OPTION);
        }
    }
    
    
    
    public JdbcTemplate getJdbcTemplate()
    {
        if(spustac.NapojeneNaDatabazu==false)
        {
            return null;
        }
        return  new DatabazaJdbcTemplate().vratJdbcTemplate("nova");
    }
    public JdbcTemplate getJdbcTemplateTesting()
    {
         if(spustac.NapojeneNaDatabazu==false)
        {
            return null;
        }
        return new DatabazaJdbcTemplate().vratJdbcTemplate("testy");
    }
}
