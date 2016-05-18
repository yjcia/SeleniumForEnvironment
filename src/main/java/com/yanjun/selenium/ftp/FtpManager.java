package com.yanjun.selenium.ftp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by YanJun on 2016/5/18.
 */
public interface FtpManager {
    List<WebElement> getFtpDataTdList(WebDriver driver);
}
