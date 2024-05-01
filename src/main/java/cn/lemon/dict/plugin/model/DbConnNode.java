package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DbConnNode {

    private String jdbcDriverClassName;
    private String userName;
    private String pwd;
    private String url;
}
