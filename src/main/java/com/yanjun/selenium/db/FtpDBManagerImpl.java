package com.yanjun.selenium.db;

import com.yanjun.selenium.model.Ftp;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Types;

/**
 * Created by YanJun on 2016/5/19.
 */
public class FtpDBManagerImpl implements FtpDBManager {

    private JdbcTemplate jdbcTemplate;

    public int findFtpDataById(int id) {
        Ftp ftp = jdbcTemplate.queryForObject("select * from t_ftp where id = ?",
                new Object[]{id},new int[]{Types.INTEGER}, Ftp.class);

        return ftp.getId();
    }



    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
