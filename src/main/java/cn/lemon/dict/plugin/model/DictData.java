package cn.lemon.dict.plugin.model;

import java.util.Objects;

public class DictData {

    private String typeCode;
    private String dictName;
    private Object dictValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DictData dictData = (DictData) o;
        return Objects.equals(typeCode, dictData.typeCode)
                && Objects.equals(dictName, dictData.dictName)
                && Objects.equals(dictValue, dictData.dictValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeCode, dictName, dictValue);
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Object getDictValue() {
        return dictValue;
    }

    public void setDictValue(Object dictValue) {
        this.dictValue = dictValue;
    }

    @Override
    public String toString() {
        return "DictData{" +
                "typeCode='" + typeCode + '\'' +
                ", dictName='" + dictName + '\'' +
                ", dictValue=" + dictValue +
                '}';
    }
}
