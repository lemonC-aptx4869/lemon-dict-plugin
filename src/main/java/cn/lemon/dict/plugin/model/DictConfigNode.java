package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DictConfigNode {

    private DbConnNode dbConn;
    private String typeCodeField;
    private String dictLabelField;
    private String dictValueField;
    private String dictSql;
    private String outputPackName;
    private boolean override;
}
