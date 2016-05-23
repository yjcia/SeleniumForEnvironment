package com.yanjun.selenium.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by YanJun on 2016/5/19.
 */
public class FtpUIManagerImpl implements FtpUIManager {

    public List<WebElement> getLastFtpDataTdList(WebDriver driver) {
        List<WebElement> newFtpTdList = driver.findElements(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr[last()]/td"));
        //去掉勾选框,id和修改图片
        newFtpTdList.remove(0);
        newFtpTdList.remove(0);
        newFtpTdList.remove(newFtpTdList.size() - 1);

        return newFtpTdList;
    }

    public List<WebElement> getLastFtpFullDataTdList(WebDriver driver) {
        List<WebElement> newFtpTdList = driver.findElements(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr[last()]/td"));
        return newFtpTdList;
    }

    public List<WebElement> getFtpDataTrList(WebDriver driver) {
        List<WebElement> ftpTableTdList
                = driver.findElements(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr"));
        return ftpTableTdList;
    }

    public WebElement getFtpAddBtn(WebDriver driver) {
        return driver.findElement(By.xpath("/html/body/div[1]/div[1]/div/div/button[1]"));
    }

    public WebElement getFtpRemoveBtn(WebDriver driver) {
        return driver.findElement(By.id("removeButton"));
    }

    public WebElement getLastFtpFullDataTrList(WebDriver driver) {
        WebElement ftpLastTrDara = driver.findElement(By.xpath("//*[@id=\"ftp_table\"]/tbody/tr[last()]"));
        return ftpLastTrDara;
    }

    public WebElement getSelectRow(WebDriver driver) {
        WebElement ftpSelectRow = driver.findElement(By.className("selected"));
        return ftpSelectRow;
    }
}

