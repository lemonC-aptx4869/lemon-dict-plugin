package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DictConfigNode {
    //数据库连接配置
    private DbConnNode dbConn;
    //字典类型字段
    private String typeCodeField;
    //字典中文字段
    private String dictLabelField;
    //字典字段
    private String dictValueField;
    //字典查询sql
    private String dictSql;
    //输出包名
    private String outputPackName;
    //是否清空包名重新生成
    private boolean override;
}
