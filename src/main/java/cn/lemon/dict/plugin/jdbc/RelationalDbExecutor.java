package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DictConfigNode;
import cn.lemon.dict.plugin.model.DictData;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RelationalDbExecutor implements DbExecutor {

    protected DictConfigNode dictConfigNode;

    public RelationalDbExecutor(DictConfigNode dictConfigNode) {
        this.dictConfigNode = dictConfigNode;
    }

    public Map<String, Set<DictData>> dictSearch() {
        Connection conn = null;
        try {
            Class.forName(dictConfigNode.getDbConn().getJdbcDriverClassName());
            conn = DriverManager.getConnection(dictConfigNode.getDbConn().getUrl(), dictConfigNode.getDbConn().getUserName(), dictConfigNode.getDbConn().getPwd());

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(dictConfigNode.getDictSql());
            Statement statement = conn.createStatement();
            statement.execute(sqlBuilder.toString());
            ResultSet rs = statement.getResultSet();

            Map<String, Set<DictData>> dictDataMap = new HashMap<String, Set<DictData>>();
            while (rs.next()) {
                String typeCode = rs.getString(dictConfigNode.getTypeCodeField());
                if (!dictDataMap.containsKey(typeCode)) dictDataMap.put(typeCode, new HashSet<DictData>());

                DictData dictData = new DictData();
                dictData.setTypeCode(typeCode);
                dictData.setDictName(rs.getString(dictConfigNode.getDictLabelField()));
                dictData.setDictValue(rs.getObject(dictConfigNode.getDictValueField()));
                dictDataMap.get(typeCode).add(dictData);
            }
            return dictDataMap;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
