package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@Data
@ToString
public class DictData {
    //字典type值
    private String typeCode;
    //字典中文值
    private String dictName;
    //字典值
    private Object dictValue;

}
