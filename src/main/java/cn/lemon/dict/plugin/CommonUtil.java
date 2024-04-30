package cn.lemon.dict.plugin;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.XmlUtil;
import cn.lemon.dict.plugin.model.DbConn;
import cn.lemon.dict.plugin.model.DictConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        try {
            Document document = XmlUtil.readXML(config);
            Element root = document.getDocumentElement();

            DictConfig dictConfig = new DictConfig();
            //db-conn
            Element dbConnNode = (Element) root.getElementsByTagName("conn").item(0);
            if (ObjectUtil.isEmpty(dbConnNode) || dbConnNode.getChildNodes().getLength() == 0) {
                Logger.LOG.error(String.format(Constant.REQUIRE_PARAMETER_ERROR_MSG_TMP, "db conn"));
            }
            DbConn dbConn = new DbConn();
            dbConn.setJdbcDriverClassName(dbConnNode.getElementsByTagName("jdbcDriverClassName").item(0).getTextContent());
            dbConn.setUrl(dbConnNode.getElementsByTagName("url").item(0).getTextContent());
            dbConn.setUserName(dbConnNode.getElementsByTagName("userName").item(0).getTextContent());
            dbConn.setPwd(dbConnNode.getElementsByTagName("pwd").item(0).getTextContent());
            dictConfig.setDbConn(dbConn);
            //typeCode
            Node typeCodeNode = root.getElementsByTagName("typeCodeField").item(0);
            dictConfig.setTypeCodeField(typeCodeNode.getTextContent());
            //dictName
            Node dictNameNode = root.getElementsByTagName("dictLabelField").item(0);
            dictConfig.setDictLabelField(dictNameNode.getTextContent());
            //dictValue
            Node dictValueNode = root.getElementsByTagName("dictValueField").item(0);
            dictConfig.setDictValueField(dictValueNode.getTextContent());
            //outputPackName
            Node outputPackNameNode = root.getElementsByTagName("outputPackName").item(0);
            dictConfig.setOutputPackName(outputPackNameNode.getTextContent());
            //dictSql
            Node dictSqlNode = root.getElementsByTagName("dictSql").item(0);
            dictConfig.setDictSql(dictSqlNode.getTextContent());
            //override
            Node overrideNode = root.getElementsByTagName("override").item(0);
            dictConfig.setOverride(Boolean.getBoolean(overrideNode.getTextContent()));
            Logger.LOG.info(dictConfig.toString());
            return dictConfig;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
