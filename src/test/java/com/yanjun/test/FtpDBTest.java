package com.yanjun.test;

import com.yanjun.selenium.db.FtpDBManager;
import com.yanjun.selenium.model.Ftp;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by YanJun on 2016/5/19.
 */
public class FtpDBTest {

    private FtpDBManager ftpDBManager;
    @Before
    public void setUp(){
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ftpDBManager = (FtpDBManager)context.getBean("ftpDBManager");

    }

    @Test
    public void testFtpFindById(){
        Ftp ftp = ftpDBManager.findFtpDataById(12);
        //Assert.assertNotNull(ftp);
        System.out.println(ftp);
    }

    @Test
    public void testFtpFindBySearchParam(){
        List<Ftp> ftpList = ftpDBManager.findFtpBySearchInput("%kff16%");
        //Assert.assertNotNull(ftp);
        System.out.println(ftpList);
    }
}
