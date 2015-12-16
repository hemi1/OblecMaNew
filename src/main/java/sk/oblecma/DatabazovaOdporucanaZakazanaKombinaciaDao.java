/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotovy test
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 *
 * @author Slavomír
 */
public class DatabazovaOdporucanaZakazanaKombinaciaDao implements OdporucenaZakazanaKombinaciaDao{

 //   private BeanPropertyRowMapper<KombinaciaANG> KombinaciarowMapper=BeanPropertyRowMapper.newInstance(KombinaciaANG.class);
    private BeanPropertyRowMapper PocasierowMapper;
    public DatabazovaOdporucanaZakazanaKombinaciaDao() {
  
       PocasierowMapper = new BeanPropertyRowMapper();
       PocasierowMapper.setMappedClass(Pocasie.class);
    }
    
    
    private boolean odporucena =true; // inak zakazana
    
    private void zapisKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela)
    {
        
           String vlozKombinaciuSql = "insert into kombinacia values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String vratKombinaciuSql = "select id from kombinacia\n" +
        "where\n" +
        "`idUzivatela` = ? and \n" +
        "`o1` = ? and \n" +
        "`o2` = ? and \n" +
        "`o3` = ? and \n" +
        "`o4` = ? and \n" +
        "`o5` = ? and \n" +
        "`o6` = ? and \n" +
        "`o7` = ? and \n" +
        "`o8` = ? and \n" +
        "`o9` = ? and \n" +
        "`o10` = ? and \n" +
        "`o11` = ? and \n" +
        "`o12` = ? and \n" +
        "`o13` = ? and \n" +
        "`o14` = ? and \n" +
        "`o15` = ? and \n" +
        "`o16` = ? and \n" +
        "`o17` = ? and \n" +
        "`o18` = ? and \n" +
        "`o19` = ? and \n" +
        "`o20` = ? and \n" +
        "`o21` = ? and \n" +
        "`o22` = ? and \n" +
        "`o23` = ? and \n" +
        "`o24` = ? and \n" +
        "`idPodmienka` = ?\n" +
        ";";
        String vlozPodmienkySql = "INSERT INTO zoznamPodmienok \n" +
        "VALUES(null,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?,\n" +
        "?);";
        
        
        String vratPodmienkySql = "select idPodmienka from zoznampodmienok\n" +
        "where\n" +
        "`sneh` = ? and \n" +
        "`zima` = ? and \n" +
        "`teplo` = ? and \n" +
        "`tropickaHorucava` = ? and \n" +
        "`horuco` = ? and \n" +
        "`teplejsie` = ? and \n" +
        "`priemerne` = ? and \n" +
        "`chladno` = ? and \n" +
        "`velkaZima` = ? and \n" +
        "`dazd` = ? and \n" +
        "`slabyVietor` = ? and \n" +
        "`silnyVietor` = ? and \n" +
        "`velmiSilnyVietor` = ? and \n" +
        "`burlivyVietor` = ? and \n" +
        "`formalnost` = ?\n" +
        ";";
        
       
        Long idPodmienky = null;
        try{
        idPodmienky = DatabazaSpojenie.jdbcTemplate.queryForObject(vratPodmienkySql,Long.class,
                podmienkyPocasia.isSneh(),
                podmienkyPocasia.isZima(),
                podmienkyPocasia.isTeplo(),
                podmienkyPocasia.isTropickaHorucava(),
                podmienkyPocasia.isHoruco(),
                podmienkyPocasia.isTeplejsie(),
                podmienkyPocasia.isPriemerne(),
                podmienkyPocasia.isChladno(),
                podmienkyPocasia.isVelkaZima(),
                podmienkyPocasia.isDazd(),
                podmienkyPocasia.isSlabyVietor(),
                podmienkyPocasia.isSilnyVietor(),
                podmienkyPocasia.isVelmiSilnyVietor(),
                podmienkyPocasia.isBurlivyVietor(),
                formalnost              
                );
        System.out.println("Zadane podmienky maju id = "+idPodmienky);
        }catch(DataAccessException e)
        {
       //     e.printStackTrace();
            // vlozia sa podmienky
            System.out.println("Zadane podmienky tam nie su "+idPodmienky);

       if(idPodmienky==null)
       {
              try {
                DatabazaSpojenie.jdbcTemplate.update(
                vlozPodmienkySql,
                podmienkyPocasia.getRychlostVetra(),
                podmienkyPocasia.getTeplotaCezDen(),
                podmienkyPocasia.getPravdepodobnostZrazok(),
                podmienkyPocasia.isSneh(),
                podmienkyPocasia.isZima(),
                podmienkyPocasia.isTeplo(),
                podmienkyPocasia.isTropickaHorucava(),
                podmienkyPocasia.isHoruco(),
                podmienkyPocasia.isTeplejsie(),
                podmienkyPocasia.isPriemerne(),
                podmienkyPocasia.isChladno(),
                podmienkyPocasia.isVelkaZima(),
                podmienkyPocasia.isDazd(),
                podmienkyPocasia.isSlabyVietor(),
                podmienkyPocasia.isSilnyVietor(),
                podmienkyPocasia.isVelmiSilnyVietor(),
                podmienkyPocasia.isBurlivyVietor(),
                formalnost);
                
                idPodmienky = DatabazaSpojenie.jdbcTemplate.queryForObject(vratPodmienkySql,Long.class,
                podmienkyPocasia.isSneh(),
                podmienkyPocasia.isZima(),
                podmienkyPocasia.isTeplo(),
                podmienkyPocasia.isTropickaHorucava(),
                podmienkyPocasia.isHoruco(),
                podmienkyPocasia.isTeplejsie(),
                podmienkyPocasia.isPriemerne(),
                podmienkyPocasia.isChladno(),
                podmienkyPocasia.isVelkaZima(),
                podmienkyPocasia.isDazd(),
                podmienkyPocasia.isSlabyVietor(),
                podmienkyPocasia.isSilnyVietor(),
                podmienkyPocasia.isVelmiSilnyVietor(),
                podmienkyPocasia.isBurlivyVietor(),
                formalnost              
                );
            } catch (DataAccessException ex) {
                System.out.println("\nNepodarilo sa vlozit a ziskat nove podmienky !!!!!!! \n");
            }
       }
       else
       {
           System.out.println("Taketo podmienky uz tam su");
       }
        }
        
       if(idPodmienky!=null)
       {
           try{
            Long[] kombinacia = odporucenaKombinacia.getKombinacia();
            
            System.out.println(Arrays.toString(kombinacia));
            Long existuje = null;
            try{
                DatabazaSpojenie.jdbcTemplate.setFetchSize(30);
               System.out.println(DatabazaSpojenie.jdbcTemplate.getFetchSize());
             existuje = DatabazaSpojenie.jdbcTemplate.queryForObject(vratKombinaciuSql,Long.class,
         //          odporucena,
                   idUzivatela,
                   kombinacia[0],
                   kombinacia[1],
                   kombinacia[2],
                   kombinacia[3],
                   kombinacia[4],
                   kombinacia[5],
                   kombinacia[6],
                   kombinacia[7],
                   kombinacia[8],
                   kombinacia[9],
                   kombinacia[10],
                   kombinacia[11],
                   kombinacia[12],
                   kombinacia[13],
                   kombinacia[14],
                   kombinacia[15],
                   kombinacia[16],
                   kombinacia[17],
                   kombinacia[18],
                   kombinacia[19],
                   kombinacia[20],
                   kombinacia[21],
                   kombinacia[22],
                   kombinacia[23],
                   idPodmienky
                   );
            }
            catch(DataAccessException ev)
            {
              //  ev.printStackTrace();
                  System.out.println("nie je tam");
           //   existuje
                //    return;
            }
            if(existuje!=null)
            {
                System.out.println("Takato komnbinacia aj s podmienkami tam uz je");
                return;
            }
           
           DatabazaSpojenie.jdbcTemplate.update(vlozKombinaciuSql,
                   idUzivatela,
                   odporucena,
                   kombinacia[0],
                   kombinacia[1],
                   kombinacia[2],
                   kombinacia[3],
                   kombinacia[4],
                   kombinacia[5],
                   kombinacia[6],
                   kombinacia[7],
                   kombinacia[8],
                   kombinacia[9],
                   kombinacia[10],
                   kombinacia[11],
                   kombinacia[12],
                   kombinacia[13],
                   kombinacia[14],
                   kombinacia[15],
                   kombinacia[16],
                   kombinacia[17],
                   kombinacia[18],
                   kombinacia[19],
                   kombinacia[20],
                   kombinacia[21],
                   kombinacia[22],
                   kombinacia[23],
                   idPodmienky
                   );
           
           }
           catch(DataAccessException ec)
           {
               ec.printStackTrace();
           }
           
       }
        
    }
    
    private List<KombinaciaANG> vratKombinaciu(Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela)
    {
           String vratKombinacieSql = "select * from Kombinacia where idPodmienka = ? and povolena = ? and idUzivatela = ?";
        
        String vratPodmienkySql = "select idPodmienka from zoznampodmienok \n" +
        "where\n" +
        "`sneh` = ? and \n" +
        "`zima` = ? and \n" +
        "`teplo` = ? and \n" +
        "`tropickaHorucava` = ? and \n" +
        "`horuco` = ? and \n" +
        "`teplejsie` = ? and \n" +
        "`priemerne` = ? and \n" +
        "`chladno` = ? and \n" +
        "`velkaZima` = ? and \n" +
        "`dazd` = ? and \n" +
        "`slabyVietor` = ? and \n" +
        "`silnyVietor` = ? and \n" +
        "`velmiSilnyVietor` = ? and \n" +
        "`burlivyVietor` = ? and \n" +
        "`formalnost` = ?\n" +
        ";";
        
        Long idPodmienky = null;
        try{
        idPodmienky = DatabazaSpojenie.jdbcTemplate.queryForObject(vratPodmienkySql,Long.class,
                podmienkyPocasia.isSneh(),
                podmienkyPocasia.isZima(),
                podmienkyPocasia.isTeplo(),
                podmienkyPocasia.isTropickaHorucava(),
                podmienkyPocasia.isHoruco(),
                podmienkyPocasia.isTeplejsie(),
                podmienkyPocasia.isPriemerne(),
                podmienkyPocasia.isChladno(),
                podmienkyPocasia.isVelkaZima(),
                podmienkyPocasia.isDazd(),
                podmienkyPocasia.isSlabyVietor(),
                podmienkyPocasia.isSilnyVietor(),
                podmienkyPocasia.isVelmiSilnyVietor(),
                podmienkyPocasia.isBurlivyVietor(),
                formalnost              
                );
        System.out.println("Zadane podmienky maju id = "+idPodmienky);
        
      try{
            List<KombinaciaANG> query = DatabazaSpojenie.jdbcTemplate.query(vratKombinacieSql,new KombinaciaANGMapper(),idPodmienky,odporucena,idUzivatela);
            for(KombinaciaANG kom : query)
            {
                System.out.println(kom.toString());
            }
            
            if(query!=null)
            {
                return query;
            }
            
            return new ArrayList<>();
      }catch(DataAccessException ek)
      {
          ek.printStackTrace();
      }
        
        
        }catch(DataAccessException e)
        {
         //   e.printStackTrace();
            System.out.println("\n********\nTaketo podmienky neexistuju alebo je niekde chyba v kode  \n*****\n");
        }
               
        
        
    
        return new ArrayList<>();
    }
    
    @Override
    public List<KombinaciaANG> vratOdporuceneKombinacie(Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela) {
        odporucena = true;
        return vratKombinaciu(podmienkyPocasia, formalnost,idUzivatela);
    }
    
    @Override
    public void OdporucKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela) {
        odporucena=true;
        zapisKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost,idUzivatela);
    }

    @Override
    public void ZakazKombinaciu(KombinaciaANG odporucenaKombinacia, Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela) {
         odporucena=false;
        zapisKombinaciu(odporucenaKombinacia, podmienkyPocasia, formalnost,idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratZakazaneKombinacie(Pocasie podmienkyPocasia, boolean formalnost,Long idUzivatela) {
        odporucena = false;
        return vratKombinaciu(podmienkyPocasia, formalnost,idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratVsetkyOdporuceneKombinacie(Long idUzivatela) {
        odporucena=true;
        return vratVsetkyKombinacie(idUzivatela);
    }

    @Override
    public List<KombinaciaANG> vratVsetkyZakazaneKombinacie(Long idUzivatela) {
        odporucena=false;
        return vratVsetkyKombinacie(idUzivatela);
    }

    private List<KombinaciaANG> vratVsetkyKombinacie(Long idUzivatela) {
       String vratVsetkySql = "select * from kombinacia where idUzivatela = ? and povolena = ?";
       
       try{
        List<KombinaciaANG> query = DatabazaSpojenie.jdbcTemplate.query(vratVsetkySql, new KombinaciaANGMapper(),idUzivatela,odporucena);
        return query;
       }
       catch(DataAccessException e)
       {
           System.out.println("nepodarilo sa ziskat pozadovane kombinacie");
           return new ArrayList<>();
       }
       
    }

    @Override
    public void presunDoZakazanej(Long idKombinacia) {
       odporucena=true;
       presunKombinaciu(idKombinacia);
    }

    @Override
    public void presunDoOdporucanej(Long idKombinacia) {
        odporucena=false;
       presunKombinaciu(idKombinacia);
    }

    @Override
    public void odstranOdporucanu(Long idKombinacia) {
        odstranKombinaciu(idKombinacia);
    }

    @Override
    public void odstranZakazanu(Long idKombinacia) {
        odstranKombinaciu(idKombinacia);
    }

    private void odstranKombinaciu(Long idKombinacia) {
        String odstranKombinaciuSql= "delete from kombinacia where id = ?";
        
        DatabazaSpojenie.jdbcTemplate.update(odstranKombinaciuSql,idKombinacia);
        
    }

    private void presunKombinaciu(Long idKombinacia) {
        String presunKombinaciuSql  = "update kombinacia set povolena = ? where id = ?";
        DatabazaSpojenie.jdbcTemplate.update(presunKombinaciuSql,!odporucena,idKombinacia);
    }

    @Override
    public Pocasie vratPocasiePreKombinaciu(Long idKombinacia) {
        String vratIdPodmienokSql = "select idPodmienka from kombinacia where id = ?";
        String vratPcasieSql = "select * from zoznamPodmienok where idPodmienka  = ?";
        
        Long idPodmienky= DatabazaSpojenie.jdbcTemplate.queryForObject(vratIdPodmienokSql,Long.class,idKombinacia);
        
        Pocasie query = DatabazaSpojenie.jdbcTemplate.queryForObject(vratPcasieSql, new PocasiePreKombinaciuMapper(),idPodmienky);
        if(query==null)
        {
        return new Pocasie();
        }
        
        return query;
    }
    
}
