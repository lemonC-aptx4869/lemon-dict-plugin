package cn.lemon.dict.plugin;

import org.apache.maven.plugin.AbstractMojo;

public abstract class AbstractMojoPlugin extends AbstractMojo {

    public AbstractMojoPlugin() {
        Logger.LOG = getLog();
    }
}
