package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DictData;

import java.util.Map;
import java.util.Set;
/**
 * @Description: 数据库执行抽象接口
 * @Author: lemonC
 * @Date: 2024/5/1
 */
public interface DbExecutor {

    Map<String, Set<DictData>> dictSearch();
}
