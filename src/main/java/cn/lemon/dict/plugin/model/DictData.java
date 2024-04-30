package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@Data
@ToString
public class DictData {

    private String typeCode;
    private String dictName;
    private Object dictValue;

}
