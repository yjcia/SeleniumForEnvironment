package com.yanjun.selenium.util;

import com.yanjun.selenium.common.SeleniumAttribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by YanJun on 2016/5/23.
 */
public class ParamUtil {

    private static Document doc;

    static {
        SAXReader reader = new SAXReader();
        try {
            doc = reader.read(ParamUtil.class.getClassLoader().getResourceAsStream("parameters.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, String>> genParamMapForFtpAdd(String type, String objName)
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        List<Map<String, String>> paramMapList = new ArrayList<Map<String, String>>();
        Map<String, String> objMap = new LinkedHashMap<String, String>();
        List<List<Element>> objElements = getObjElements(type, objName);
        for (int i = 0; i < objElements.size(); i++) {
            for (Element e : objElements.get(i)) {
                String elementValue = e.getStringValue();
                String id = e.attributeValue("id");
                objMap.put(id, elementValue);
            }
        }
        paramMapList.add(objMap);
        return paramMapList;
    }

    public static List<String> genParamForFtpSearch(String type, String objName)
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        List<String> paramStrList = new ArrayList<String>();
        List<List<Element>> objElements = getObjElements(type, objName);
        for (int i = 0; i < objElements.size(); i++) {
            for (Element e : objElements.get(i)) {
                paramStrList.add(e.getText());
            }
        }
        return paramStrList;
    }

    public static List<Map<String, String>> genParamForFtpUpdate(String type, String objName)
            throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        List<Map<String, String>> paramMapList = new ArrayList<Map<String, String>>();
        List<List<Element>> objElements = getObjElements(type, objName);
        for (int i = 0; i < objElements.size(); i++) {
            for (Element e : objElements.get(i)) {
                Map<String, String> objMap = new LinkedHashMap<String, String>();
                String elementValue = e.getStringValue();
                String id = e.attributeValue("id");
                objMap.put(id, elementValue);
                paramMapList.add(objMap);
            }
        }

        return paramMapList;
    }

    public static String getModelClassName(String objName) {
        String className = objName.substring(0, 1).toUpperCase() + objName.substring(1);
        return SeleniumAttribute.MODEL_PACKAGE + className;
    }

    public static List<List<Element>> getObjElements(String type, String objName) {
        Element rootElement = doc.getRootElement();
        List<Element> paramList = rootElement.elements("parameter");
        List<List<Element>> objElements = new ArrayList<List<Element>>();
        for (Element parameter : paramList) {
            if (parameter.attributeValue("type").equals(type)) {
                List<Element> objList = parameter.elements("object");
                for (Element objElement : objList) {
                    if (objElement.attributeValue("name").equals(objName)) {
                        objElements.add(objElement.elements());
                    }
                }
            }
        }
        return objElements;
    }
}
