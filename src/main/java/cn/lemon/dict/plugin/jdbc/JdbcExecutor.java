package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DictConfigNode;
import cn.lemon.dict.plugin.model.DictData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    public Map<String, Set<DictData>> dictSearch() {
        Map<String, Set<DictData>> dictMap = new HashMap<>();
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
            Map<String, Set<DictData>> dictDataMap = new HashMap();
            while (rs.next()) {
                String typeCode = rs.getString(dictConfigNode.getTypeCodeField());
                if (!dictDataMap.containsKey(typeCode)) dictDataMap.put(typeCode, new HashSet());

                DictData dictData = new DictData();
                dictData.setTypeCode(typeCode);
                dictData.setDictName(rs.getString(dictConfigNode.getDictLabelField()));
                dictData.setDictValue(rs.getObject(dictConfigNode.getDictValueField()));
                dictDataMap.get(typeCode).add(dictData);
            }
            dictMap.putAll(dictDataMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dictMap;
    }
}
