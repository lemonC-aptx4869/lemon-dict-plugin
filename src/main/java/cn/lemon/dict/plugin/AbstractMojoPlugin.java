package cn.lemon.dict.plugin;

import org.apache.maven.plugin.AbstractMojo;

public abstract class AbstractMojoPlugin extends AbstractMojo {

    //日志初始化
    public AbstractMojoPlugin() {
        Logger.LOG = getLog();
    }
}
