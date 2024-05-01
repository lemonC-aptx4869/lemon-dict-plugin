package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class DictType {

    private String typeCode;
    private Class javaType;
}
