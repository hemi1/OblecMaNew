/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;
// hotove testy
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Slavomír
 */
public class DatabazovaHistoriaDao implements HistoriaDao{

    private Long idUzivatela = Uzivatel.globalUser;
    
    public void nastavUzivatela(Long id)
    {
        idUzivatela=id;
    }
    
    @Override
    public List<Kombinacia> vratCeluHistoriu() {
        List<Kombinacia> vrat = new ArrayList<>();
        
        String vratKombinacieZoDnaSql="select * from kombinaciahistoria where idUzivatela = ?";
        
        vrat=DatabazaSpojenie.jdbcTemplate.query(vratKombinacieZoDnaSql, new KombinaciaMapper(),idUzivatela);
        
        return vrat;
    }

    @Override
    public List<Kombinacia> vratHistoriuOdoDna(LocalDate datum) {
        List<Kombinacia> vrat = new ArrayList<>();
        
        String vratKombinacieZoDnaSql="select * from kombinaciahistoria where idUzivatela = ? and datum >= ?";
        Date pomoc = new Date(datum.getYear()-1900, datum.getMonthValue()-1, datum.getDayOfMonth());
        vrat=DatabazaSpojenie.jdbcTemplate.query(vratKombinacieZoDnaSql, new KombinaciaMapper(),idUzivatela,pomoc);
        
        return vrat;
    }

    @Override
    public List<Kombinacia> vratKombinacieZoDna(LocalDate datum) {
        List<Kombinacia> vrat = new ArrayList<>();
        Date pomoc = new Date(datum.getYear()-1900, datum.getMonthValue()-1, datum.getDayOfMonth());
        String vratKombinacieZoDnaSql="select * from kombinaciahistoria where idUzivatela = ? and datum = ?";
        
        vrat=DatabazaSpojenie.jdbcTemplate.query(vratKombinacieZoDnaSql, new KombinaciaMapper(),idUzivatela,pomoc);
        
        return vrat;
    }

    @Override
    public void vymazHistoriu() {
        String vymazHistoriuSql = "delete from kombinaciahistoria where id>0 and idUzivatela = ?";
        
        DatabazaSpojenie.jdbcTemplate.update(vymazHistoriuSql,idUzivatela);
    }

    @Override
    public void nastavDlzkuHistorie(int pocetDni) {
        String nastvaDlzkuSql = "update historianastavenia set dlzka = ? where id = 1";
        DatabazaSpojenie.jdbcTemplate.update(nastvaDlzkuSql,pocetDni);
    }

    @Override
    public void pridaj(Kombinacia kombinacia) {
        String pridajSql = "insert into kombinaciahistoria values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        Long[] zoznam = kombinacia.vratZoznamId();
        LocalDate local = kombinacia.getDatum();

        Date datum = new Date(local.getYear()-1900,local.getMonthValue()-1,local.getDayOfMonth());
      
        DatabazaSpojenie.jdbcTemplate.update(pridajSql,
            idUzivatela,    
            zoznam[0],    
            zoznam[1],
            zoznam[2], 
            zoznam[3], 
            zoznam[4], 
            zoznam[5], 
            zoznam[6], 
            zoznam[7], 
            zoznam[8], 
            zoznam[9],     
            zoznam[10],    
            zoznam[11],
            zoznam[12], 
            zoznam[13], 
            zoznam[14], 
            zoznam[15], 
            zoznam[16], 
            zoznam[17], 
            zoznam[18], 
            zoznam[19],         
            zoznam[20],    
            zoznam[21],
            zoznam[22], 
            zoznam[23],    
            datum);
        
    }

    @Override
    public void pridaj(List<Kombinacia> zoznamKombinacii) {
        for(Kombinacia kombinacia : zoznamKombinacii)
        {
            pridaj(kombinacia);
        }
    }

    @Override
    public void odober(Kombinacia kombinacia) {
        String odoberSql = "delete from kombinaciahistoria where id = ? and idUzivatela = ? ";
        try{
        DatabazaSpojenie.jdbcTemplate.update(odoberSql,kombinacia.vratId(),idUzivatela);
        }
        catch(DataAccessException e)
        {
            System.out.println("ODOBERANIE ERROR");
        }
    }

    @Override
    public void odober(List<Kombinacia> zoznamKombinacii) {
        for(Kombinacia kombinacia : zoznamKombinacii)
        {
            odober(kombinacia);
        }
    }

    @Override
    public boolean maRovnaku(Kombinacia kombinacia) {
        List<Kombinacia> celaHistoria = vratCeluHistoriu();
        
        for(Kombinacia aktualna : celaHistoria)
        {
            if(aktualna.jeRovnaka(kombinacia))
            {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public Map<Long, List<Kombinacia>> vratCeluHistoriuMapu() {
       Map<Long, List<Kombinacia>> mapa = new HashMap<>();
       
       String vratUzivatelov = "select idUzivatela from kombinaciahistoria group by idUzivatela";
       
       List<Long> uzivatelia = DatabazaSpojenie.jdbcTemplate.query(vratUzivatelov,new LongIdUzivatelaMapper());
       
       Long zalohaID = this.idUzivatela;
       
       for(Long idUzivatela : uzivatelia)
       {
           if(idUzivatela==null)
           {
               continue;
           }
           
           nastavUzivatela(idUzivatela);
           mapa.put(idUzivatela, vratCeluHistoriu());
       }
       
       this.idUzivatela=zalohaID;
       
       return mapa;
    }
    
}
