package com.yanjun.selenium;

import com.yanjun.selenium.common.SeleniumAttribute;
import com.yanjun.selenium.db.FtpDBManager;
import com.yanjun.selenium.model.Ftp;
import com.yanjun.selenium.ui.FtpUIManager;
import com.yanjun.selenium.util.DriverUtil;
import com.yanjun.selenium.util.ParamUtil;
import com.yanjun.selenium.util.PropertyUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;


/**
 * Created by YanJun on 2016/5/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class EnvironmentFTPTest {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentFTPTest.class);
    private static WebDriver webDriver;
    private static JavascriptExecutor jsExecutor;
    private String testRemoteUrl;
    private String testUrl;
    private String testMode;
    private String testAppName;
    private static final String PATH = "ftp";
    private static final String URL_SEPARATOR = "/";

    @Autowired
    private FtpUIManager ftpUIManager;

    @Autowired
    private FtpDBManager ftpDBManager;


    @Before
    public void setUp() {
        try {
            testRemoteUrl = PropertyUtil.getValue(SeleniumAttribute.TEST_REMOTE_URL);
            testUrl = PropertyUtil.getValue(SeleniumAttribute.TEST_URL);
            testAppName = PropertyUtil.getValue(SeleniumAttribute.TEST_APP_NAME);
            String needTestUrl = testUrl + URL_SEPARATOR + testAppName + URL_SEPARATOR + PATH;
            testMode = PropertyUtil.getValue(SeleniumAttribute.TEST_MODE);
            if (testMode.equals(SeleniumAttribute.TEST_MODE_LOCAL)) {
                webDriver = DriverUtil.getDriver(SeleniumAttribute.FIREFOX);
            } else if (testMode.equals(SeleniumAttribute.TEST_MODE_NODE)) {
                webDriver = DriverUtil.getRemoteDriver(SeleniumAttribute.FIREFOX, testRemoteUrl);
            }
            jsExecutor = (JavascriptExecutor) webDriver;


            if (webDriver != null) {
                webDriver.get(needTestUrl);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testFtpAdd() {
        try {
            logger.debug("begin ftp add test case");
            WebElement addBtn = ftpUIManager.getFtpAddBtn(webDriver);
            List<WebElement> beforeFtpTableTdList = ftpUIManager.getFtpDataTrList(webDriver);
            int beforeAddFtpCount = beforeFtpTableTdList.size();
            addBtn.click();
            List<Map<String, String>> ftpAddParamList = ParamUtil.genParamMapForFtpAdd("insert", "ftp");
            for (Map<String, String> paramMap : ftpAddParamList) {
                List<String> inputParamsList = new ArrayList<String>();
                Set<String> inputIdSetForAdd = paramMap.keySet();
                for (String id : inputIdSetForAdd) {
                    jsExecutor.executeScript("document.getElementById('" + id + "').value='"
                            + paramMap.get(id) + "'");
                    inputParamsList.add(paramMap.get(id));
                }
                jsExecutor.executeScript("document.getElementById('ftpSave').click()");
                Thread.sleep(1000);
                webDriver.navigate().refresh();
                //判断新增数据行的数据是否和测试数据吻合
                List<WebElement> ftpDataTdList = ftpUIManager.getLastFtpDataTdList(webDriver);
                for (int i = 0; i < ftpDataTdList.size(); i++) {
                    Assert.assertEquals(ftpDataTdList.get(i).getText(), inputParamsList.get(i));
                }

            }
            //不分页的情况下,判断界面上是不是多出一行新数据
            List<WebElement> afterFtpTableTdList = ftpUIManager.getFtpDataTrList(webDriver);
            int afterAddFtpCount = afterFtpTableTdList.size();
            Assert.assertEquals(ftpAddParamList.size(), afterAddFtpCount - beforeAddFtpCount);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            webDriver.close();
        }
    }

    @Test
    public void testFtpSingleRemove() {
        logger.debug("begin ftp remove test case");
        try {
            //非分页情况下,记录被删除前的记录总数
            int beforeRemoveSize = ftpUIManager.getFtpDataTrList(webDriver).size();
            //选中要被删除的行,默认最后一行
            WebElement ftpLastRow = ftpUIManager.getLastFtpFullDataTrList(webDriver);
            WebElement idElement = ftpLastRow.findElement(By.xpath("td[2]"));
            int selectId = Integer.parseInt(idElement.getText());
            ftpLastRow.click();
            WebElement removeBtn = ftpUIManager.getFtpRemoveBtn(webDriver);
            removeBtn.click();
            Thread.sleep(1000);
            //非分页情况下,记录被删除后的
            int afterRemoveSize = ftpUIManager.getFtpDataTrList(webDriver).size();
            Assert.assertEquals(1, beforeRemoveSize - afterRemoveSize);
            //数据库验证是否还存在选中的记录
            Assert.assertNull(ftpDBManager.findFtpDataById(selectId));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            webDriver.close();
        }

    }

    @Test
    public void testFtpGetSelectRow() {
        //选中要被删除的行,默认最后一行
        WebElement ftpLastRow = ftpUIManager.getLastFtpFullDataTrList(webDriver);
        ftpLastRow.click();
        WebElement selectRow = ftpUIManager.getSelectRow(webDriver);
        WebElement idElement = selectRow.findElement(By.xpath("td[2]"));
        System.out.println(idElement.getText());
        webDriver.close();
    }

    @Test
    public void testFtpSearch() {
        logger.debug("begin ftp search test case");
        try {
            //获得搜索框
            WebElement searchInputElement =
                    webDriver.findElement(By.className("search")).findElement(By.xpath("input"));
            //输入搜作条件触发搜索功能
            List<String> searchTextList = ParamUtil.genParamForFtpSearch("search","ftp");
            for(String searchkey:searchTextList){
                searchInputElement.clear();
                searchInputElement.sendKeys(searchkey);
                Thread.sleep(2000);
                List<WebElement> searchElementUIList = ftpUIManager.getFtpDataTrList(webDriver);
                List<Ftp> searchElementDBList = ftpDBManager.findFtpBySearchInput(searchkey);
                //判断查询结果集数量
                Assert.assertEquals(searchElementDBList.size(), searchElementUIList.size());
                //判断查询结果id
                if (searchElementDBList.size() == searchElementUIList.size()) {
                    for (int i = 0; i < searchElementDBList.size(); i++) {
                        int idForUI = Integer.parseInt(searchElementUIList.
                                get(i).findElement(By.xpath("td[2]")).getText());
                        int idForDB = searchElementDBList.get(i).getId();
                        Assert.assertEquals(idForUI, idForDB);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            webDriver.close();
        }
    }

    @Test
    public void testFtpUpdate() {
        logger.debug("begin ftp update test case");
        try {
            //选中要被更新的行,默认最后一行
            List<Map<String, String>> ftpUpdateParamList = ParamUtil.genParamForFtpUpdate("update", "ftp");
            for(Map<String, String> paramMap : ftpUpdateParamList){
                Set<String> inputIdSetForAdd = paramMap.keySet();
                for (String id : inputIdSetForAdd) {
                    WebElement ftpLastRow = ftpUIManager.getLastFtpFullDataTrList(webDriver);
                    ftpLastRow.click();
                    WebElement idElement = ftpLastRow.findElement(By.xpath("td[2]"));
                    int selectId = Integer.parseInt(idElement.getText());
                    //点击修改按钮
                    WebElement updateBtn = ftpLastRow.findElement(By.xpath("td[last()]/span[@id='update']"));
                    updateBtn.click();
                    Thread.sleep(1000);
                    //更新remark字段,此处可以使用参数化完成
                    jsExecutor.executeScript("document.getElementById('"+id+"').value='"+paramMap.get(id)+"'");
                    jsExecutor.executeScript("document.getElementById('updateFtpBtn').click()");
                    Thread.sleep(1000);
                    Ftp ftp = ftpDBManager.findFtpDataById(selectId);
                    Assert.assertEquals(paramMap.get(id), ftp.getRemark());
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            webDriver.close();
        }


    }


}
