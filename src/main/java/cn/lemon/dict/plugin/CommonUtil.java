package cn.lemon.dict.plugin;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import cn.lemon.dict.plugin.model.DbConn;
import cn.lemon.dict.plugin.model.DictConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

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

    public static DictConfig readConfig(File config) {
        if (config == null) Logger.LOG.error(String.format(Constant.REQUIRE_ERROR_MSG_TMP, "dict config file"));
        Document document = XmlUtil.readXML(config);

        DictConfig dictConfig = new DictConfig();
        //db-conn
        NodeList nodeList = document.getElementsByTagName("conn");
        if (ObjectUtil.isEmpty(nodeList))
            Logger.LOG.error(String.format(Constant.REQUIRE_PARAMETER_ERROR_MSG_TMP, "db conn"));
        DbConn dbConn = new DbConn();
        BeanDesc beanDesc = new BeanDesc(DbConn.class);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (beanDesc.getProp(node.getNodeName()) == null) continue;
            beanDesc.getProp(node.getNodeName()).setValue(dbConn, node.getNodeValue());
        }
        dictConfig.setDbConn(dbConn);
        //typeCode
        dictConfig.setTypeCodeField(document.getElementsByTagName("typeCodeField").item(0).getNodeValue());
        //dictName
        dictConfig.setTypeCodeField(document.getElementsByTagName("dictLabelField").item(0).getNodeValue());
        //dictValue
        dictConfig.setTypeCodeField(document.getElementsByTagName("dictValueField").item(0).getNodeValue());
        //outputPackName
        dictConfig.setTypeCodeField(document.getElementsByTagName("outputPackName").item(0).getNodeValue());
        //dictSql
        dictConfig.setTypeCodeField(document.getElementsByTagName("dictSql").item(0).getNodeValue());

        return dictConfig;
    }

}
