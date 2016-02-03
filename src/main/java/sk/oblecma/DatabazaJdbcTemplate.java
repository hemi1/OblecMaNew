/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import javax.swing.JOptionPane;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Slavomír
 */
public class DatabazaJdbcTemplate {

    
    private JdbcTemplate jdbcTemplate;
    public JdbcTemplate vratJdbcTemplate(String nazovDatabazy) {
        
     //  String databaseURL = "jdbc:mysql://localhost/nova";
        String user = "root";
        String password = "Rastislav1";

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
      //  dataSource.setUrl(databaseURL);
        dataSource.setCreateDatabaseIfNotExist(true);
        dataSource.setConnectionAttributes("jdbc:mysql://localhost");
        dataSource.setDatabaseName(nazovDatabazy);
     //   dataSource.setDatabaseName("nova");
        try{
        jdbcTemplate = new JdbcTemplate(dataSource);
        
        // Uzivatel
        jdbcTemplate.update("create table if not exists pouzivatel(id int primary key not null auto_increment,meno text ,heslo text,muz bool)");
        jdbcTemplate.execute("ALTER TABLE pouzivatel AUTO_INCREMENT = 1;");
        // Oblecenie
        jdbcTemplate.update("create table if not exists oblecenie(\n" +
        "idOblecenia int primary key not null auto_increment,\n" +
        "vlastnikID int,\n" +
        "nazov varchar(50),\n" +
        "kategoria int,\n" +
        "nepremokave boolean,\n" +
        "neprefuka boolean,\n" +
        "zateplene boolean,\n" +
        "formalne boolean,\n" +
        "nove boolean,\n" +
        "nosene boolean,\n" +
        "stare boolean,\n" +
        "pocetObleceniBezPrania int,\n" +
        "vPrani boolean,\n" +
        "mozeSaPoziciavat boolean,\n" +
        "idObrazka int)" );
        jdbcTemplate.execute("ALTER TABLE oblecenie AUTO_INCREMENT = 1;");
                // Kategorie
        jdbcTemplate.update("create table if not exists kategoria(\n" +
        "cislo int primary key not null auto_increment,\n" +
        "nazov varchar(20),\n" +
        "maximalnyPocetNoseniBezPrania int, \n"+
        "lokacia int,\n" +
        "vrstva int,\n" +
        "pouzivatelID int \n"+
        ");");
        
       jdbcTemplate.execute("ALTER TABLE kategoria AUTO_INCREMENT = 1;");

            // Obrazok
        jdbcTemplate.update("create table if not exists obrazok(\n" +
        "id int primary key not null auto_increment,\n" +
        "subor text\n" +
        ");");
        jdbcTemplate.execute("ALTER TABLE obrazok AUTO_INCREMENT = 1;");
        // Podmienky pre odporucane kombinacie
        
        jdbcTemplate.update("create table if not exists zoznamPodmienok\n" +
        "(\n" +
        "idPodmienka int primary key not null auto_increment,\n" +
        "rychlostVetra int,\n" +
        "teplotaCezDen int,\n" +
        "pravdepodobnostZrazok int,\n" +
        "\n" +
        "  \n" +
        " sneh bool,\n" +
        " zima bool,\n" +
        " teplo bool,\n" +
        "   \n" +
        " tropickaHorucava bool,\n" +
        " horuco bool,\n" +
        " teplejsie bool,\n" +
        " priemerne bool,\n" +
        " chladno bool,\n" +
        " velkaZima bool, \n" +
        "    \n" +
        "  dazd bool,\n" +
        "\n" +
        " slabyVietor bool,\n" +
        " silnyVietor bool,\n" +
        " velmiSilnyVietor bool,\n" +
        " burlivyVietor bool,\n" +
        "formalnost bool\n" +
        "\n" +
        ");");
        
        jdbcTemplate.execute("ALTER TABLE zoznamPodmienok AUTO_INCREMENT = 1;");
        // Kombinacie
        
        jdbcTemplate.update("create table if not exists Kombinacia(\n" +
        " id int primary key not null auto_increment,\n" +
        " idUzivatela int, \n"+
        " povolena bool, \n"+
        " o1 int,\n" +
        " o2 int,\n" +
        " o3 int, \n" +
        " o4 int,\n" +
        " o5 int,\n" +
        " o6 int,\n" +
        " o7 int,\n" +
        " o8 int,\n" +
        " o9 int,\n" +
        " o10 int,\n" +
        " o11 int,\n" +
        " o12 int,\n" +
        " o13 int,\n" +
        " o14 int,\n" +
        " o15 int,\n" +
        " o16 int,\n" +
        " o17 int,\n" +
        " o18 int,\n" +
        " o19 int,\n" +
        " o20 int,\n" +
        " o21 int,\n" +
        " o22 int,\n" +
        " o23 int,\n" +
        " o24 int,\n" +
        " idPodmienka int not null\n" +
        " );");
        jdbcTemplate.execute("ALTER TABLE Kombinacia AUTO_INCREMENT = 1;");
        // Historia 
        jdbcTemplate.update("create table if not exists historianastavenia(id int primary key,dlzka int);");
        jdbcTemplate.update("create table if not exists KombinaciaHistoria(\n" +
        " id int primary key not null auto_increment,\n" +
        " idUzivatela int ,\n" +
        " o1 int,\n" +
        " o2 int,\n" +
        " o3 int, \n" +
        " o4 int,\n" +
        " o5 int,\n" +
        " o6 int,\n" +
        " o7 int,\n" +
        " o8 int,\n" +
        " o9 int,\n" +
        " o10 int,\n" +
        " o11 int,\n" +
        " o12 int,\n" +
        " o13 int,\n" +
        " o14 int,\n" +
        " o15 int,\n" +
        " o16 int,\n" +
        " o17 int,\n" +
        " o18 int,\n" +
        " o19 int,\n" +
        " o20 int,\n" +
        " o21 int,\n" +
        " o22 int,\n" +
        " o23 int,\n" +
        " o24 int,\n" +
        " datum datetime\n" +
        ");");
        jdbcTemplate.execute("ALTER TABLE KombinaciaHistoria AUTO_INCREMENT = 1;");
        
         jdbcTemplate.update("create table if not exists obuv(\n" +
        "idObuvy int primary key not null auto_increment,\n" +
        "vlastnikID int,\n" +
        "nazov varchar(50),\n" +
        "nepremokave boolean,\n" +
        "vetrane boolean,\n" +
        "zateplene boolean\n"+
        ");");
        jdbcTemplate.execute("ALTER TABLE oblecenie AUTO_INCREMENT = 1;");
        }

        catch(DataAccessException e)
        {
            System.err.println("Data accessexceprtion inner");
            spustac.NapojeneNaDatabazu=false;
        }
        catch(Exception e)
        {
            System.out.println("\n nie je dobry jdbc \n ");
            spustac.NapojeneNaDatabazu=false;
        }
        
        try{ 
            if(spustac.NapojeneNaDatabazu)
            {
                jdbcTemplate.update("insert into kategoria values(-4,'ine',0,0,0,-1);");
            }
        }
        catch(DataAccessException e)
        {
            
        }
        try{ 
            if(spustac.NapojeneNaDatabazu)
            {
                jdbcTemplate.update("insert into historianastavenia values(1,7);");
            }
        }
        catch(DataAccessException e)
        {
            
        }
 
        if(spustac.NapojeneNaDatabazu==false)
        {
            JOptionPane.showMessageDialog(null, "Nepodarilo sa pripojit k databaze ! \n Aplikacia pojde na pameti" ,"Chyba spojenia",JOptionPane.OK_OPTION);
        }
        return jdbcTemplate;
    }
    
}
