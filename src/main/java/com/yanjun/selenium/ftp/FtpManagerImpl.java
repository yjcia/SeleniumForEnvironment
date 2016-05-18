package com.yanjun.selenium.ftp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by YanJun on 2016/5/18.
 */


public class FtpManagerImpl implements FtpManager {

    public List<WebElement> getLastFtpDataTdList(WebDriver driver){
        List<WebElement> newFtpTdList = driver.findElements(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr[last()]/td"));
        //去掉勾选框,id和修改图片
        newFtpTdList.remove(0);
        newFtpTdList.remove(0);
        newFtpTdList.remove(newFtpTdList.size()-1);

        return newFtpTdList;
    }

    public List<WebElement> getFtpDataTdList(WebDriver driver) {
        List<WebElement> ftpTableTdList
                = driver.findElements(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr"));
        return ftpTableTdList;
    }

    public WebElement getFtpAddBtn(WebDriver driver) {
        return driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/button[1]"));
    }
}
