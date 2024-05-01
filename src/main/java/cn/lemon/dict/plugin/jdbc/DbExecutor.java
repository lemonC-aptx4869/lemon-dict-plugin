package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DictData;

import java.util.Map;
import java.util.Set;

/**
 * @Description: 数据库查询接口
 * @Author: lemonC
 * @Date: 2024/5/1
 */
public interface DbExecutor {

    /**
     * @Description: 使用dictSql查询字典
     * @Author: lemonC
     * @Date: 2024/5/1
     */
    Map<String, Set<DictData>> dictSearch();
}
