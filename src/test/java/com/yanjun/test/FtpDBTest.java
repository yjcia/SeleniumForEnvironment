package com.yanjun.test;

import com.yanjun.selenium.db.FtpDBManager;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
    public void testFtpAdd(){

    }
}
