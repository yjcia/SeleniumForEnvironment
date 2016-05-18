package com.yanjun.selenium.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by YanJun on 2016/5/17.
 */
public class PropertyUtil {
    private static Properties prop = new Properties();
    static{
        try {
            prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream("test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String propKey){
        return String.valueOf(prop.get(propKey));
    }
}
