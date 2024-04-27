package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.Logger;
import cn.lemon.dict.plugin.model.DbConnInfo;
import cn.lemon.dict.plugin.model.DictData;

import java.sql.*;
import java.util.*;

public abstract class RelationalDbExecutor implements DbExecutor {

    private DbConnInfo dbConnInfo;

    public RelationalDbExecutor(DbConnInfo dbConnInfo) {
        this.dbConnInfo = dbConnInfo;
    }

    public Map<String, Set<DictData>> dictSearch() {
        Connection conn = null;
        try {
            Logger.LOG.info("conn......");
            Class.forName(dbConnInfo.getJdbcDriverClassName());
            conn = DriverManager.getConnection(dbConnInfo.getUrl(), dbConnInfo.getUserName(), dbConnInfo.getPwd());

            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append(dbConnInfo.getDictSql());
            Statement statement = conn.createStatement();
            statement.execute(sqlBuilder.toString());
            ResultSet rs = statement.getResultSet();

            Map<String, Set<DictData>> dictDataMap = new HashMap<String, Set<DictData>>();
            while (rs.next()) {
                String typeCode = rs.getString(dbConnInfo.getTypeCodeField());
                if (!dictDataMap.containsKey(typeCode)) dictDataMap.put(typeCode, new HashSet<DictData>());

                DictData dictData = new DictData();
                dictData.setTypeCode(typeCode);
                dictData.setDictName(rs.getString(dbConnInfo.getDictLabelField()));
                dictData.setDictValue(rs.getObject(dbConnInfo.getDictValueField()));
                Logger.LOG.info("dictData===========》》" + dictData.toString());
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
