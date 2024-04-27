package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.Logger;
import cn.lemon.dict.plugin.model.DbConn;
import cn.lemon.dict.plugin.model.DictConfig;
import cn.lemon.dict.plugin.model.DictData;

import java.sql.*;
import java.util.*;

public abstract class RelationalDbExecutor implements DbExecutor {

    private DictConfig dictConfig;

    public RelationalDbExecutor(DictConfig dictConfig) {
        this.dictConfig = dictConfig;
    }

    public Map<String, Set<DictData>> dictSearch() {
        Connection conn = null;
        try {
            Logger.LOG.info("conn......");
            Class.forName(dictConfig.getDbConn().getJdbcDriverClassName());
            conn = DriverManager.getConnection(dictConfig.getDbConn().getUrl(), dictConfig.getDbConn().getUserName(), dictConfig.getDbConn().getPwd());

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(dictConfig.getDictSql());
            Statement statement = conn.createStatement();
            statement.execute(sqlBuilder.toString());
            ResultSet rs = statement.getResultSet();

            Map<String, Set<DictData>> dictDataMap = new HashMap<String, Set<DictData>>();
            while (rs.next()) {
                String typeCode = rs.getString(dictConfig.getTypeCodeField());
                if (!dictDataMap.containsKey(typeCode)) dictDataMap.put(typeCode, new HashSet<DictData>());

                DictData dictData = new DictData();
                dictData.setTypeCode(typeCode);
                dictData.setDictName(rs.getString(dictConfig.getDictLabelField()));
                dictData.setDictValue(rs.getObject(dictConfig.getDictValueField()));
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
