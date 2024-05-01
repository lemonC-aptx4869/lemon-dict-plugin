package cn.lemon.dict.plugin.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DbConnNode {
    //驱动类
    private String jdbcDriverClassName;
    //用户名
    private String userName;
    //密码
    private String pwd;
    //url
    private String url;
}
