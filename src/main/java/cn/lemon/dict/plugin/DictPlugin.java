package cn.lemon.dict.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import cn.lemon.dict.plugin.jdbc.DbExecutor;
import cn.lemon.dict.plugin.jdbc.RelationalDbExecutor;
import cn.lemon.dict.plugin.model.DictConfig;
import cn.lemon.dict.plugin.model.DictData;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "dict-plugin", defaultPhase = LifecyclePhase.COMPILE)
public class DictPlugin extends AbstractMojoPlugin {
    /**
     * @doc 源码输出文件
     */
    @Parameter(defaultValue = "${project.basedir}/src/", property = "sourceDirectory")
    private File sourceDirectory;
    /**
     * @doc 文件输出
     */
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/annotations", property = "targetDirectory")
    private File generateSourceDir;
    /**
     * @doc 项目
     */
    @Parameter(property = "project")
    private MavenProject project;
    /**
     * @doc 配置文件
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/lemon-dict/config.xml", required = true)
    private File config;

    public void execute() throws MojoExecutionException {
        Logger.LOG.info("---------------------------------parse config---------------------------------");
        DictConfig dictConfig = CommonUtil.readConfig(config);
        Logger.LOG.info("---------------------------------parse config---------------------------------");
        //查询字典
        Logger.LOG.info("connecting database......");
        DbExecutor executor = new RelationalDbExecutor(dictConfig);
        Map<String, Set<DictData>> dictDataMap = executor.dictSearch();
        //生成代码
        Logger.LOG.info("---------------------------------generate source---------------------------------");
        dictDataMap.keySet().stream().forEach(typeCode -> {
            try {
                String className = CommonUtil.toHump(typeCode);
                className = className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase();
                Logger.LOG.info("className\t" + className);
                TypeSpec.Builder enumTypeSpecBuilder = TypeSpec.enumBuilder(className)
                        .addModifiers(Modifier.PUBLIC);
                enumTypeSpecBuilder.addField(String.class, CommonUtil.toHump(dictConfig.getDictLabelField()), Modifier.PRIVATE);
                enumTypeSpecBuilder.addField(Object.class, CommonUtil.toHump(dictConfig.getDictValueField()), Modifier.PRIVATE);
                enumTypeSpecBuilder.addMethod(
                        MethodSpec.constructorBuilder()
                                .addParameter(String.class, dictConfig.getDictLabelField())
                                .addParameter(Object.class, dictConfig.getDictValueField())
                                .addStatement("this.$N = $N", dictConfig.getDictLabelField(), dictConfig.getDictLabelField())
                                .addStatement("this.$N = $N", dictConfig.getDictValueField(), dictConfig.getDictValueField())
                                .build()
                );
                dictDataMap.get(typeCode).stream().forEach(dictData -> {
                    Logger.LOG.info("dictData\t" + dictData);
                    enumTypeSpecBuilder.addEnumConstant(
                            CommonUtil.toConstantLabel(dictData.getTypeCode() + "_" + dictData.getDictValue()),
                            TypeSpec.anonymousClassBuilder("$S,$S", dictData.getDictName(), dictData.getDictValue()).build()
                    );
                });
                JavaFile javaFile = JavaFile.builder(dictConfig.getOutputPackName(), enumTypeSpecBuilder.build()).build();
                if (dictConfig.isOverride()) generateSourceDir.deleteOnExit();
                if (!generateSourceDir.exists()) generateSourceDir.mkdirs();
                javaFile.writeTo(generateSourceDir);
            } catch (IOException e) {
                Logger.LOG.error(e);
                throw new RuntimeException(e);
            }
        });
    }
}
