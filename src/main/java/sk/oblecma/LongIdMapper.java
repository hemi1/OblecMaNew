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
public class LongIdMapper implements RowMapper<Long> {

    @Override
    public Long mapRow(ResultSet rs, int i) throws SQLException {
       return rs.getLong("id");
    }
    
}
