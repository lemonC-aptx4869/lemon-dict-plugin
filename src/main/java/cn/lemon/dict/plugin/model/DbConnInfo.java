package cn.lemon.dict.plugin.model;

public class DbConnInfo {

    private String typeCodeField;
    private String dictLabelField;
    private String dictValueField;
    private String jdbcDriverClassName;
    private String userName;
    private String pwd;
    private String url;
    private String dictSql;

    public String getDictSql() {
        return dictSql;
    }

    public void setDictSql(String dictSql) {
        this.dictSql = dictSql;
    }

    public String getJdbcDriverClassName() {
        return jdbcDriverClassName;
    }

    public void setJdbcDriverClassName(String jdbcDriverClassName) {
        this.jdbcDriverClassName = jdbcDriverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    @Override
    public String toString() {
        return "DbConnInfo{" +
                "typeCodeField='" + typeCodeField + '\'' +
                ", dictLabelField='" + dictLabelField + '\'' +
                ", dictValueField='" + dictValueField + '\'' +
                ", userName='" + userName + '\'' +
                ", pwd='" + pwd + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
