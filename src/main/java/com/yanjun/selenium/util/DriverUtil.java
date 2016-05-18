package com.yanjun.selenium.util;

import com.yanjun.selenium.common.SeleniumAttribute;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by YanJun on 2016/5/17.
 */
public class DriverUtil {
    public static WebDriver getDriver(String browserType){
        if(browserType.equals(SeleniumAttribute.CHROME)){
            System.setProperty(SeleniumAttribute.CHROME_DRIVER, SeleniumAttribute.CHROME_DRIVER_PATH);
            return new ChromeDriver();
        }else if(browserType.equals(SeleniumAttribute.FIREFOX)){
            System.setProperty(SeleniumAttribute.FIREFOX_DRIVER, SeleniumAttribute.FIREFOX_DRIVER_PATH);
            return new FirefoxDriver();
        }
        return null;
    }
}
