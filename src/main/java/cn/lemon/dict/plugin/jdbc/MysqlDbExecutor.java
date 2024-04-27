package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DbConn;
import cn.lemon.dict.plugin.model.DictConfig;

public class MysqlDbExecutor extends RelationalDbExecutor {


    public MysqlDbExecutor(DictConfig dictConfig) {
        super(dictConfig);
    }
}
