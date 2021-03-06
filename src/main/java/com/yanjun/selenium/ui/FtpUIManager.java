package com.yanjun.selenium.ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by YanJun on 2016/5/18.
 */
public interface FtpUIManager {
    List<WebElement> getLastFtpDataTdList(WebDriver driver);

    List<WebElement> getLastFtpFullDataTdList(WebDriver driver);

    List<WebElement> getFtpDataTrList(WebDriver driver);

    WebElement getFtpAddBtn(WebDriver driver);

    WebElement getFtpRemoveBtn(WebDriver driver);

    WebElement getLastFtpFullDataTrList(WebDriver driver);

    WebElement getSelectRow(WebDriver driver);

}
