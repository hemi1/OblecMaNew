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
public class DatabazoveOblecenieDao implements OblecenieDao {

    private BeanPropertyRowMapper rowMapper;

    public DatabazoveOblecenieDao() {
        rowMapper = new BeanPropertyRowMapper();
        rowMapper.setMappedClass(Oblecenie.class);
    }

    @Override
    public void pridajOblecenie(Oblecenie noveOblecenie, Long ID) {
        noveOblecenie.setVlastnikID(ID);
        String pridajSql = "INSERT INTO oblecenie\n"
                + "VALUES\n"
                + "( null ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ?,\n"
                + "?);";
        DatabazaSpojenie.jdbcTemplate.update(pridajSql,
                noveOblecenie.getVlastnikID(),
                noveOblecenie.getNazov(),
                noveOblecenie.getKategoria(),
                noveOblecenie.isNepremokave(),
                noveOblecenie.isNeprefuka(),
                noveOblecenie.isZateplene(),
                noveOblecenie.isFormalne(),
                noveOblecenie.isNove(),
                noveOblecenie.isNosene(),
                noveOblecenie.isStare(),
                noveOblecenie.getPocetObleceniBezPrania(),
                noveOblecenie.isvPrani(),
                noveOblecenie.isMozeSaPoziciavat(),
                noveOblecenie.getIdObrazka()
        );
    }

    @Override
    public void vyhodOblecenie(Long idOblecenia, Long ID) {

        String vyhodOblecenieSql = "delete from oblecenie where idOblecenia = ? and vlastnikID = ?";
        DatabazaSpojenie.jdbcTemplate.update(vyhodOblecenieSql, idOblecenia, ID);

    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia(Long ID) {
        List<Oblecenie> vrat = vrat = new ArrayList<>();
        DatabazaSpojenie.jdbcTemplate.getFetchSize();
        String dajVsetkySql = "select * from oblecenie where vlastnikID = ? ";
        try {
            vrat = DatabazaSpojenie.jdbcTemplate.query(dajVsetkySql, rowMapper, ID);
        } catch (DataAccessException e) {

        }
        return vrat;
    }

    @Override
    public List<Oblecenie> dajObleceniePodlaKategorie(Long kategoria, Long ID) {
        List<Oblecenie> vrat = null;

        String dajPodlaKategorieSql = "select * from oblecenie where vlastnikID = ? and kategoria = ?";
        try {
            vrat = DatabazaSpojenie.jdbcTemplate.query(dajPodlaKategorieSql, rowMapper, ID, kategoria);

        } catch (DataAccessException e) {
            vrat = new ArrayList<>();
        }

        return vrat;
    }

    @Override
    public void odstranKategoriuOblecenia(Long cisloKategorie, Long ID) {
        String odstranKategoriuSql = "update oblecenie set kategoria = ? where kategoria =? and vlastnikID = ?";

        DatabazaSpojenie.jdbcTemplate.update(odstranKategoriuSql, Kategoria.ineFinal, cisloKategorie, ID);

    }

    @Override
    public void nastavKategoriuOblecenie(Long cisloOblecenia, Long cisloKategorie, Long ID) {
        String updateKategorieSql = "update oblecenie set kategoria = ? where idOblecenia = ? and  kategoria = ? and vlastnikID = ?";

        DatabazaSpojenie.jdbcTemplate.update(updateKategorieSql, cisloKategorie, cisloOblecenia, Kategoria.ineFinal, ID);
    }

    @Override
    public List<Oblecenie> dajVsetkyOblecenia() {
        List<Oblecenie> vrat = vrat = new ArrayList<>();
        DatabazaSpojenie.jdbcTemplate.getFetchSize();
        String dajVsetkySql = "select * from oblecenie";
        try {
            vrat = DatabazaSpojenie.jdbcTemplate.query(dajVsetkySql, rowMapper);
        } catch (DataAccessException e) {

        }
        return vrat;
    }

    @Override
    public void upravOblecenie(Oblecenie upraveneOblecenie) {
        String upravOblecenieSql = "UPDATE oblecenie\n"
                + "SET\n"
                + "`vlastnikID` =  ? ,\n"
                + "`nazov` =  ?,\n"
                + "`kategoria` = ?,\n"
                + "`nepremokave` = ?,\n"
                + "`neprefuka` = ?,\n"
                + "`zateplene` = ?,\n"
                + "`formalne` = ?,\n"
                + "`nove` =  ?,\n"
                + "`nosene` = ?,\n"
                + "`stare` = ?,\n"
                + "`pocetObleceniBezPrania` = ?,\n"
                + "`vPrani` = ?,\n"
                + "`mozeSaPoziciavat` = ?,\n"
                + "`idObrazka` = ? \n"
                + "WHERE `idOblecenia` = ?;";

        try {
            DatabazaSpojenie.jdbcTemplate.update(upravOblecenieSql,
                    upraveneOblecenie.getVlastnikID(),
                    upraveneOblecenie.getNazov(),
                    upraveneOblecenie.getKategoria(),
                    upraveneOblecenie.isNepremokave(),
                    upraveneOblecenie.isNeprefuka(),
                    upraveneOblecenie.isZateplene(),
                    upraveneOblecenie.isFormalne(),
                    upraveneOblecenie.isNove(),
                    upraveneOblecenie.isNosene(),
                    upraveneOblecenie.isStare(),
                    upraveneOblecenie.getPocetObleceniBezPrania(),
                    upraveneOblecenie.isvPrani(),
                    upraveneOblecenie.isMozeSaPoziciavat(),
                    upraveneOblecenie.getIdObrazka(),
                    upraveneOblecenie.getIdOblecenia()
            );
        } catch (DataAccessException e) {
            System.out.println("\n nepodarilo sa upravit oblecenie \n");
        }

    }

    @Override
    public Oblecenie dajOblecenie(Long id) {
        String dajOblecenieSql = "select * from oblecenie where idOblecenia = ?";
        
        try{
            return (Oblecenie) DatabazaSpojenie.jdbcTemplate.queryForObject(dajOblecenieSql, rowMapper,id);
        }
        catch(DataAccessException e)
        {
            return null;
        }
    }
}
