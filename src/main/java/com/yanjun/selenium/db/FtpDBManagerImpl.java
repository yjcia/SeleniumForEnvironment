package com.yanjun.selenium.db;

import com.yanjun.selenium.model.Ftp;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by YanJun on 2016/5/19.
 */
public class FtpDBManagerImpl implements FtpDBManager {

    private JdbcTemplate jdbcTemplate;

    public Ftp findFtpDataById(int id) {
        Ftp ftp = null;
        try {
            ftp = (Ftp) jdbcTemplate.queryForObject("select * from t_ftp where id = ?",
                    new Object[]{id}, new int[]{Types.INTEGER}, new FtpRowMapper());
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }

        return ftp;
    }



    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public class FtpRowMapper implements RowMapper {

        public Object mapRow(ResultSet rs, int value) throws SQLException {
            if(rs.next()){
                Ftp ftp = new Ftp();
                ftp.setId(rs.getInt("id"));
                ftp.setHostName(rs.getString("host_name"));
                ftp.setUser(rs.getString("user"));
                ftp.setPassword(rs.getString("password"));
                ftp.setPort(rs.getString("port"));
                ftp.setPath(rs.getString("path"));
                ftp.setRemark(rs.getString("remark"));
                return ftp;
            }
           return null;
        }
    }
}
