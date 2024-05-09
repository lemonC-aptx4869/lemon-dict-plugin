# lemon-dict-plugin

<br/>

## 什么是lemon-dict-plugin

日常中常常使用枚举类代替硬编码中的字典校验以及赋值，但是无法和字典表中进行同步。dict-plugin插件自动根据字典sql生成对应声明的字典枚举类。

## 使用

### 创建配置文件

默认读取resource/lemon-dict/config.xml配置文件

### 配置内容

```
<generateConfig>
    <!--    字典配置，可存在多个-->
    <dictConfig>
        <!--        数据库连接-->
        <dbConn>
            <!--            数据库连接-数据库jdbc引擎-->
            <jdbcDriverClassName>com.mysql.cj.jdbc.Driver</jdbcDriverClassName>
            <!--            数据库连接-jdbcUrl-->
            <url>jdbc:mysql://localhost:3306</url>
            <!--            数据库连接-用户名-->
            <userName>root</userName>
            <!--            数据库连接-密码-->
            <pwd>abc123</pwd>
        </dbConn>
        <!--        sql中字典分类对应字段,根据分类生成类名-->
        <typeCodeField>TYPE_CODE</typeCodeField>
        <!--        sql中字典中文对应字段-->
        <dictLabelField>DICT_NAME</dictLabelField>
        <!--        sql中字典值对应字段-->
        <dictValueField>DICT_VALUE</dictValueField>
        <!--        输出包名-->
        <outputPackName>cn.lemon.dict.enums</outputPackName>
        <!--        sql字典查询-->
        <dictSql>select type.TYPE_CODE,dict.* from dictionary_t dict</dictSql>
    </dictConfig>
</generateConfig>
```

### 声明插件

pom文件中

```
<build>
        <plugins>
            <plugin>
                <groupId>cn.lemon.dict.plugin</groupId>
                <artifactId>lemon-dict-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>compile</phase>
                        <goals>
                            <goal>dict-plugin</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

即可在idea的maven插件中使用，或是在编译期间自动触发。
