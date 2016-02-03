/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.oblecma;

import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 *
 * @author Rastislav
 */
public class DatabazovyObuvDao implements ObuvDao{
private BeanPropertyRowMapper rowMapper;
public DatabazovyObuvDao() {
        rowMapper = new BeanPropertyRowMapper();
        rowMapper.setMappedClass(Obuv.class);
    }
    @Override
    public void pridajObuv(Obuv novaObuv, Long ID) {
novaObuv.setVlastnikID(ID);
        String pridajSql = "INSERT INTO obuv\n"
                + "VALUES\n"
                + "( null ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + " ? ,\n"
                + "?);";
        DatabazaSpojenie.jdbcTemplate.update(pridajSql,
                novaObuv.getVlastnikID(),
                novaObuv.getNazov(),
                novaObuv.isNepremokave(),
                novaObuv.isVetrane(),
                novaObuv.isZateplene()              
                );
    }

    @Override
    public void vyhodObuv(Long idObuvy, Long ID) {
        String vyhodObuvSql = "delete from obuv where idObuvy = ? and vlastnikID = ?";
        DatabazaSpojenie.jdbcTemplate.update(vyhodObuvSql, idObuvy, ID);    }

    @Override
    public List<Obuv> dajVsetkyObuvyPodlaId(Long ID) {
    List<Obuv> vrat = vrat = new ArrayList<>();
        DatabazaSpojenie.jdbcTemplate.getFetchSize();
        String dajVsetkySql = "select * from obuv where vlastnikID = ? ";
        try {
            vrat = DatabazaSpojenie.jdbcTemplate.query(dajVsetkySql, rowMapper, ID);
        } catch (DataAccessException e) {

        }
        return vrat;
    }

    @Override
    public List<Obuv> dajVsetkyObuvy() {
        List<Obuv> vrat = vrat = new ArrayList<>();
        DatabazaSpojenie.jdbcTemplate.getFetchSize();
        String dajVsetkySql = "select * from obuv";
        try {
            vrat = DatabazaSpojenie.jdbcTemplate.query(dajVsetkySql, rowMapper);
        } catch (DataAccessException e) {

        }
        return vrat;
    }

    @Override
    public void upravObuvy(Obuv upravenaObuv) {
            String upravObuvySql = "UPDATE obuv\n"
                + "SET\n"
                + "`vlastnikID` =  ? ,\n"
                + "`nazov` =  ?,\n"
                + "`nepremokave` = ?,\n"
                + "`vetrane` = ?,\n"
                + "`zateplene` = ?\n"
                + "WHERE `idObuvy` = ?;";

        try {
            DatabazaSpojenie.jdbcTemplate.update(upravObuvySql,
                    upravenaObuv.getVlastnikID(),
                    upravenaObuv.getNazov(),
                    upravenaObuv.isNepremokave(),
                    upravenaObuv.isVetrane(),
                    upravenaObuv.isZateplene()
                   
            );
        } catch (DataAccessException e) {
            System.out.println("\n nepodarilo sa upravit obuv \n");
        }    }

    @Override
    public Obuv dajObuv(Long id) {
            String dajObuvSql = "select * from obuv where idObuvy = ?";
        
        try{
            return (Obuv) DatabazaSpojenie.jdbcTemplate.queryForObject(dajObuvSql, rowMapper,id);
        }
        catch(DataAccessException e)
        {
            return null;
        }
    }
    
}
