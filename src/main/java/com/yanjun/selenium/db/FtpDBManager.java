package com.yanjun.selenium.db;

import com.yanjun.selenium.model.Ftp;

import java.util.List;

/**
 * Created by YanJun on 2016/5/19.
 */
public interface FtpDBManager {
    Ftp findFtpDataById(int id);

    List<Ftp> findFtpBySearchInput(String likeParam);

}
