package com.yanjun.test;

import com.yanjun.selenium.util.ParamUtil;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by YanJun on 2016/5/23.
 */
public class FtpParamTest {

    @Test
    public void testParamGenMap(){
        try {
            List<Map<String,String>> paramList = ParamUtil.genParamMapForFtpAdd("insert","ftp");
            System.out.println(paramList.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testParamSearchList(){
        try {
            List<String> paramList = ParamUtil.genParamForFtpSearch("search","ftp");
            System.out.println(paramList.size());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
