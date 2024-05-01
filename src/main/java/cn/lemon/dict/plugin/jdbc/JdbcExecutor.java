package cn.lemon.dict.plugin.jdbc;

import cn.hutool.core.util.ObjectUtil;
import cn.lemon.dict.plugin.CommonUtil;
import cn.lemon.dict.plugin.model.DictConfigNode;
import cn.lemon.dict.plugin.model.DictData;
import cn.lemon.dict.plugin.model.DictType;

import java.sql.*;
import java.util.*;

/**
 * @Description: 关系型数据库查询实现
 * @Author: lemonC
 * @Date: 2024/5/1
 */
public class JdbcExecutor implements DbExecutor {

    /**
     * @doc 字典配置
     */
    protected DictConfigNode dictConfigNode;

    public JdbcExecutor(DictConfigNode dictConfigNode) {
        this.dictConfigNode = dictConfigNode;
    }

    /**
     * @Description: 使用dictSql查询字典-jdbc实现
     * @Author: lemonC
     * @Date: 2024/5/1
     */
    public Map<DictType, Set<DictData>> dictSearch() {
        Map<DictType, Set<DictData>> dictMap = new HashMap<>();
        try {
            //加载驱动
            Class.forName(dictConfigNode.getDbConn().getJdbcDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(dictConfigNode.getDbConn().getUrl(), dictConfigNode.getDbConn().getUserName(), dictConfigNode.getDbConn().getPwd())) {
            //查询sql
            Statement statement = conn.createStatement();
            statement.execute(dictConfigNode.getDictSql());
            ResultSet rs = statement.getResultSet();
            //转换对象
            while (rs.next()) {
                ResultSetMetaData rsm = rs.getMetaData();
                DictData dictData = new DictData();
                DictType dictType = new DictType();
                for (int i = 1; i <= rsm.getColumnCount(); i++) {
                    String colName = rsm.getColumnName(i);
                    //typeCode
                    if (Objects.equals(colName, dictConfigNode.getTypeCodeField())) {
                        dictType.setTypeCode(CommonUtil.toHump(rs.getString(colName), true));
                    }
                    //dictName
                    if (Objects.equals(colName, dictConfigNode.getDictLabelField())) {
                        dictData.setDictName(rs.getString(colName));
                    }
                    //dictCode
                    if (Objects.equals(colName, dictConfigNode.getDictValueField())) {
                        dictData.setDictValue(rs.getObject(colName));
                    }
                    //javaType
                    if (ObjectUtil.isNotEmpty(dictData.getDictValue())) {
                        dictType.setJavaType(CommonUtil.convertToJavaType(rsm.getColumnType(i)));
                    }
                }
                if (ObjectUtil.isNotEmpty(dictType.getTypeCode())) {
                    if (!dictMap.containsKey(dictType)) {
                        dictMap.put(dictType, new HashSet<>());
                    }
                    dictMap.get(dictType).add(dictData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dictMap;
    }
}
