package cn.lemon.dict.plugin.model;

public class DictConfig {

    private DbConn dbConn;
    private String typeCodeField;
    private String dictLabelField;
    private String dictValueField;
    private String dictSql;
    private String outputPackName;
    private boolean override;

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public String getOutputPackName() {
        return outputPackName;
    }

    public void setOutputPackName(String outputPackName) {
        this.outputPackName = outputPackName;
    }

    public DbConn getDbConn() {
        return dbConn;
    }

    public void setDbConn(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    public String getTypeCodeField() {
        return typeCodeField;
    }

    public void setTypeCodeField(String typeCodeField) {
        this.typeCodeField = typeCodeField;
    }

    public String getDictLabelField() {
        return dictLabelField;
    }

    public void setDictLabelField(String dictLabelField) {
        this.dictLabelField = dictLabelField;
    }

    public String getDictValueField() {
        return dictValueField;
    }

    public void setDictValueField(String dictValueField) {
        this.dictValueField = dictValueField;
    }

    public String getDictSql() {
        return dictSql;
    }

    public void setDictSql(String dictSql) {
        this.dictSql = dictSql;
    }

    @Override
    public String toString() {
        return "DictConfig{" +
                "dbConn=" + dbConn +
                ", typeCodeField='" + typeCodeField + '\'' +
                ", dictLabelField='" + dictLabelField + '\'' +
                ", dictValueField='" + dictValueField + '\'' +
                ", dictSql='" + dictSql + '\'' +
                ", outputPackName='" + outputPackName + '\'' +
                ", override=" + override +
                '}';
    }
}
