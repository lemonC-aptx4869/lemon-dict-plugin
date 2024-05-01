package cn.lemon.dict.plugin;

import org.apache.maven.plugin.AbstractMojo;
/**
 * @Description: 抽象插件
 * @Author: lemonC
 * @Date: 2024/5/1
 */
public abstract class AbstractMojoPlugin extends AbstractMojo {

    //日志初始化
    public AbstractMojoPlugin() {
        Logger.LOG = getLog();
    }
}
