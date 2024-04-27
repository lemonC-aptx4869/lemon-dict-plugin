package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DbConnInfo;

public class MysqlDbExecutor extends RelationalDbExecutor {


    public MysqlDbExecutor(DbConnInfo dbConnInfo) {
        super(dbConnInfo);
    }
}
