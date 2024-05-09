# lemon-dict-plugin

<br/>

## 什么是lemon-dict-plugin

日常中常常使用枚举类代替硬编码中的字典校验以及赋值，但是无法和字典表中进行同步。dict-plugin插件自动根据字典sql生成对应声明的字典枚举类。

## 使用

### 创建配置文件

默认读取resource/lemon-dict/config.xml配置文件

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
