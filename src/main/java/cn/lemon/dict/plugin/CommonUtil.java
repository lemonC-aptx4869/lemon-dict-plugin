package cn.lemon.dict.plugin;

import cn.hutool.core.util.XmlUtil;
import cn.lemon.dict.plugin.model.DictConfigNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class CommonUtil {


    public static String toHump(String source) {
        StringBuilder strBuilder = new StringBuilder();
        if (source.contains("_")) {
            String[] tmp = source.split("_");
            strBuilder.append(tmp[0]);
            strBuilder.append(tmp[1].substring(0, 1).toUpperCase() + tmp[1].substring(1).toLowerCase());
            return strBuilder.toString();
        } else {
            return source;
        }
    }

    public static String toConstantLabel(String source) {
        StringBuilder strBuilder = new StringBuilder();
        if (source.contains("_")) {
            return source.toUpperCase();
        } else {
            for (int i = 0; i < source.length(); i++) {
                Character c = source.charAt(i);
                if (Character.isUpperCase(c)) {
                    strBuilder.append("_");
                }
                strBuilder.append(c);
            }
            return strBuilder.toString().toUpperCase();
        }
    }


    public static List<DictConfigNode> parseDictXml(File xmlConfig) {
        Element root = XmlUtil.readXML(xmlConfig).getDocumentElement();
        List<DictConfigNode> dictConfigs = new LinkedList<>();
        NodeList dictConfigNodes = root.getElementsByTagName("dictConfig");
        for (int i = 0; i < dictConfigNodes.getLength(); i++) {
            Node node = dictConfigNodes.item(i);
            dictConfigs.add(XmlUtil.xmlToBean(node, DictConfigNode.class));
        }
        return dictConfigs;
    }
}
