package cn.lemon.dict.plugin.jdbc;

import cn.lemon.dict.plugin.model.DictData;

import java.util.Map;
import java.util.Set;

public interface DbExecutor {

    Map<String, Set<DictData>> dictSearch();
}
