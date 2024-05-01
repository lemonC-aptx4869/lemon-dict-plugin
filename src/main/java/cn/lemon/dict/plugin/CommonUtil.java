package cn.lemon.dict.plugin;

import cn.hutool.core.util.XmlUtil;
import cn.lemon.dict.plugin.model.DictConfig;
import cn.lemon.dict.plugin.model.DictConfigNode;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Description: 通用工具类
 * @Author: lemonC
 * @Date: 2024/5/1
 */
public class CommonUtil {


    /**
     * @Description: 转换驼峰：ab_cd ===> abCd
     * @Author: lemonC
     * @Date: 2024/5/1
     */
    public static String toHump(String source, boolean isClass) {
        StringBuilder strBuilder = new StringBuilder();
        if (source.contains("_")) {
            String[] tmp = source.split("_");
            strBuilder.append(tmp[0]);
            strBuilder.append(tmp[1].substring(0, 1).toUpperCase() + tmp[1].substring(1));
            return isClass ? (strBuilder.substring(0, 1).toUpperCase() + strBuilder.substring(1)) : strBuilder.toString();
        } else {
            return isClass ? (source.substring(0, 1).toUpperCase() + source.substring(1)) : source;
        }
    }

    /**
     * @Description: 转换常量值：ab_cd ===> AB_CD、abCd ===> AB_CD
     * @Author: lemonC
     * @Date: 2024/5/1
     */
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


    /**
     * @Description: 解析字典xml文件成为字典配置
     * @Author: lemonC
     * @Date: 2024/5/1
     */
    public static DictConfig parseDictXml(File xmlConfig) {
        Element root = XmlUtil.readXML(xmlConfig).getDocumentElement();
        List<DictConfigNode> dictConfigs = new LinkedList<>();
        DictConfig dictConfig = new DictConfig();
        NodeList dictConfigNodes = root.getElementsByTagName("dictConfig");
        for (int i = 0; i < dictConfigNodes.getLength(); i++) {
            Node node = dictConfigNodes.item(i);
            dictConfigs.add(XmlUtil.xmlToBean(node, DictConfigNode.class));
        }
        dictConfig.setDictConfigs(dictConfigs);
        return dictConfig;
    }


    public static Class<?> convertToJavaType(Integer colType) {
        if (colType == null) return null;
        switch (colType) {
            case Types.ARRAY:
                return ArrayList.class;
            case Types.BIT:
                return Short.class;
            case Types.BIGINT:
                return Long.class;
            case Types.BOOLEAN:
                return Boolean.class;
            case Types.DATE:
                return Date.class;
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return LocalDateTime.class;
            case Types.DECIMAL:
            case Types.NUMERIC:
                return BigDecimal.class;
            case Types.DOUBLE:
                return Double.class;
            case Types.FLOAT:
                return Float.class;
            case Types.INTEGER:
            case Types.SMALLINT:
            case Types.TINYINT:
                return Integer.class;
            default:
                return String.class;
        }
    }


    public static void deleteFile(File file) {
        StringBuilder msg = new StringBuilder();
        msg.append(" delete ");
        msg.append(file.isDirectory() ? " directory: " : " file: ");
        msg.append(file.getAbsolutePath());
        Logger.LOG.info(msg.toString());
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        } else {
            file.delete();
        }
    }
}
