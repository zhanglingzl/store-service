package com.rxr.store.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

public class XmlUtils {

    public static void parseElement(List<Element> dataEle, Map<String, String> dataMap) {
        dataEle.forEach(element -> {
            if(element.hasContent()) {
                parseElement(element.elements(),dataMap);
            }
            dataMap.put(element.getName(), element.getTextTrim());
        });
    }

    public static String getXml(Map<String,String> elementMap) {
        Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("xml");
        document.setRootElement(root);
        elementMap.forEach((name, value) -> {
            Element element = DocumentHelper.createElement(name);
            element.setText(value);
            root.add(element);
        });
        return document.asXML();
    }
}
