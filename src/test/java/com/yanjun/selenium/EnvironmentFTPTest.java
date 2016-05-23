package com.yanjun.selenium;

import com.yanjun.selenium.common.SeleniumAttribute;
import com.yanjun.selenium.db.FtpDBManager;
import com.yanjun.selenium.model.Ftp;
import com.yanjun.selenium.ui.FtpUIManager;
import com.yanjun.selenium.util.DriverUtil;
import com.yanjun.selenium.util.PropertyUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.*;


/**
 * Created by YanJun on 2016/5/17.
 */

@RunWith(Parameterized.class)
public class EnvironmentFTPTest {
    private static final Logger logger = LoggerFactory.getLogger(EnvironmentFTPTest.class);
    private static WebDriver webDriver;
    private static JavascriptExecutor jsExecutor;
    private static String testUrl;
    private static String testPort;
    private static String testAppName;
    private static final String PATH = "ftp";
    private static final String URL_SEPARATOR = "/";
    private FtpUIManager ftpUIManager;
    private FtpDBManager ftpDBManager;
    private int insertRowCount;
    private String hostNameParam;
    private String userNameParam;
    private String passwordParam;
    private String pathParam;
    private String remarkParam;
    private String portParam;
    private static Map<String,String> inputParamMap = new LinkedHashMap<String,String>();

    public EnvironmentFTPTest(int insertRowCount, String hostNameParam,
                              String userNameParam, String passwordParam,
                              String portParam,String pathParam,
                              String remarkParam) {
        this.insertRowCount = insertRowCount;
        this.hostNameParam = hostNameParam;
        this.userNameParam = userNameParam;
        this.passwordParam = passwordParam;
        this.portParam = portParam;
        this.pathParam = pathParam;
        this.remarkParam = remarkParam;

        inputParamMap.put("inputHostName",hostNameParam);
        inputParamMap.put("inputUser",userNameParam);
        inputParamMap.put("inputPwd",passwordParam);
        inputParamMap.put("inputPort",portParam);
        inputParamMap.put("inputPath",pathParam);
        inputParamMap.put("inputRemark",remarkParam);
    }

    @Before
    public void setUp(){

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        ftpUIManager = (FtpUIManager) context.getBean("ftpUIManager");
        ftpDBManager = (FtpDBManager) context.getBean("ftpDBManager");
        webDriver = DriverUtil.getDriver(SeleniumAttribute.FIREFOX);
        jsExecutor = (JavascriptExecutor)webDriver;
        testUrl = PropertyUtil.getValue(SeleniumAttribute.TEST_URL);
        testPort = PropertyUtil.getValue(SeleniumAttribute.TEST_PORT);
        testAppName = PropertyUtil.getValue(SeleniumAttribute.TEST_APP_NAME);
        String needTestUrl = testUrl+URL_SEPARATOR+testAppName+URL_SEPARATOR+PATH;

        if(webDriver != null){
            webDriver.get(needTestUrl);
        }

    }

    @Parameterized.Parameters
    public static Collection prepareData(){
        //该二维数组的类型必须是Object类型的
        //该二维数组中的数据是为测试Calculator中的add()方法而准备的
        //该二维数组中的每一个元素中的数据都对应着构造方法ParameterTest()中的参数的位置
        //所以依据构造方法的参数位置判断，该二维数组中的第一个元素里面的第一个数据等于后两个数据的和
        //有关这种具体的使用规则，请参考JUnit4的API文档中的org.junit.runners.Parameterized类的说明
        Object[][] paramObjArr = {{1,"10.104.46.200","kff16","kff16","22","/selenium/test/path","selenium test"}};
        return Arrays.asList(paramObjArr);
    }

    @Test
    public void testFtpAdd(){
        try {
            logger.debug("begin ftp add test case");
            WebElement addBtn = ftpUIManager.getFtpAddBtn(webDriver);
            List<WebElement> beforeFtpTableTdList = ftpUIManager.getFtpDataTrList(webDriver);
            int beforeAddFtpCount = beforeFtpTableTdList.size();
            List<String> inputParamsList = new ArrayList<String>();
            addBtn.click();

            Set<String> inputIdSet = inputParamMap.keySet();
            for(String id:inputIdSet){
                jsExecutor.executeScript("document.getElementById('"+id+"').value='"+inputParamMap.get(id)+"'");
                inputParamsList.add(inputParamMap.get(id));
            }

            jsExecutor.executeScript("document.getElementById('ftpSave').click()");
            Thread.sleep(1000);
            webDriver.navigate().refresh();

            //不分页的情况下,判断界面上是不是多出一行新数据
            List<WebElement> afterFtpTableTdList = ftpUIManager.getFtpDataTrList(webDriver);
            int afterAddFtpCount = afterFtpTableTdList.size();
            Assert.assertEquals(prepareData().size(),afterAddFtpCount-beforeAddFtpCount);

            //判断新增数据行的数据是否和测试数据吻合
            List<WebElement> ftpDataTdList = ftpUIManager.getLastFtpDataTdList(webDriver);
            for(int i=0;i<ftpDataTdList.size();i++) {
                Assert.assertEquals(ftpDataTdList.get(i).getText(), inputParamsList.get(i));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFtpSingleRemove(){
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
            Assert.assertEquals(1,beforeRemoveSize-afterRemoveSize);
            //数据库验证是否还存在选中的记录
            Assert.assertNull(ftpDBManager.findFtpDataById(selectId));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testFtpGetSelectRow(){
        //选中要被删除的行,默认最后一行
        WebElement ftpLastRow = ftpUIManager.getLastFtpFullDataTrList(webDriver);
        ftpLastRow.click();
        WebElement selectRow = ftpUIManager.getSelectRow(webDriver);
        WebElement idElement = selectRow.findElement(By.xpath("td[2]"));
        System.out.println(idElement.getText());


    }
    @Test
    public void testFtpSearch(){
        logger.debug("begin ftp search test case");
        try {
            //获得搜索框
            WebElement searchInputElement =
                    webDriver.findElement(By.className("search")).findElement(By.xpath("input"));
            //输入搜作条件触发搜索功能
            searchInputElement.sendKeys("kff16");
            Thread.sleep(2000);
            List<WebElement> searchElementUIList = ftpUIManager.getFtpDataTrList(webDriver);
            List<Ftp> searchElementDBList = ftpDBManager.findFtpBySearchInput("kff16");
            //判断查询结果集数量
            Assert.assertEquals(searchElementDBList.size(),searchElementUIList.size());
            //判断查询结果id
            if(searchElementDBList.size() == searchElementUIList.size()){
                for(int i=0;i<searchElementDBList.size();i++){
                    int idForUI = Integer.parseInt(searchElementUIList.
                            get(i).findElement(By.xpath("td[2]")).getText());
                    int idForDB = searchElementDBList.get(i).getId();
                    Assert.assertEquals(idForUI,idForDB);
                }
            }
            //ftpDBManager.findFtpBySearchInput("kff16");

        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFtpUpdate(){
        logger.debug("begin ftp update test case");
        try {
            //选中要被更新的行,默认最后一行
            WebElement ftpLastRow = ftpUIManager.getLastFtpFullDataTrList(webDriver);
            ftpLastRow.click();
            WebElement idElement = ftpLastRow.findElement(By.xpath("td[2]"));
            int selectId = Integer.parseInt(idElement.getText());
            //点击修改按钮
            WebElement updateBtn = ftpLastRow.findElement(By.xpath("td[last()]/span[@id='update']"));
            updateBtn.click();
            Thread.sleep(1000);
            //更新remark字段,此处可以使用参数化完成
            jsExecutor.executeScript("document.getElementById('updateRemark').value='test update'");
            jsExecutor.executeScript("document.getElementById('updateFtpBtn').click()");
            Thread.sleep(1000);
            Ftp ftp = ftpDBManager.findFtpDataById(selectId);
            Assert.assertEquals("test update",ftp.getRemark());
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
