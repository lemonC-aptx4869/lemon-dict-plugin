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
import cn.lemon.dict.plugin.jdbc.MysqlDbExecutor;
import cn.lemon.dict.plugin.model.DbConn;
import cn.lemon.dict.plugin.model.DictConfig;
import cn.lemon.dict.plugin.model.DictData;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "dict-plugin", defaultPhase = LifecyclePhase.COMPILE)
public class DictPlugin extends AbstractMojoPlugin {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;


    public void execute() throws MojoExecutionException {
        Logger.LOG.info("DictPlugin start......");

        DictConfig dictConfig = new DictConfig();
        DbConn dbConn = new DbConn();
        dictConfig.setDbConn(dbConn);
        dictConfig.setTypeCodeField("typeCode");
        dictConfig.setDictLabelField("dictName");
        dictConfig.setDictValueField("dictValue");
        dictConfig.setOutputPackName("cn.lemon.dict.enums");
        dbConn.setJdbcDriverClassName("com.mysql.cj.jdbc.Driver");
        dbConn.setUrl("jdbc:mysql://localhost:3306");
        dbConn.setUserName("root");
        dbConn.setPwd("abc123");
        dictConfig.setDictSql("select type.TYPE_CODE as typeCode,dict.DICT_NAME as dictName,dict.DICT_VALUE as dictValue from dataassets_baseline.sym_dictionary_type_t type inner join dataassets_baseline.sym_dictionary_t dict on type.TID = dict.DICT_TYPE_ID");

        //查询字典
        DbExecutor executor = new MysqlDbExecutor(dictConfig);
        Map<String, Set<DictData>> dictDataMap = executor.dictSearch();

        //生成代码
        dictDataMap.keySet().stream().forEach(typeCode -> {
            Logger.LOG.info("typeCode====>" + typeCode);
            try {
                String className = CommonUtil.toHump(typeCode);
                className = className.substring(0, 1).toUpperCase() + className.substring(1).toLowerCase();
                Logger.LOG.info("className====>" + className);
                TypeSpec.Builder enumTypeSpecBuilder = TypeSpec.enumBuilder(className)
                        .addModifiers(Modifier.PUBLIC);
                enumTypeSpecBuilder.addField(String.class, dictConfig.getDictLabelField(), Modifier.PRIVATE);
                enumTypeSpecBuilder.addField(Object.class, dictConfig.getDictValueField(), Modifier.PRIVATE);
                enumTypeSpecBuilder.addMethod(MethodSpec.constructorBuilder()
                        .addParameter(String.class, dictConfig.getDictLabelField())
                        .addParameter(Object.class, dictConfig.getDictValueField())
                        .addStatement("this.$N = $N", dictConfig.getDictLabelField(), dictConfig.getDictLabelField())
                        .addStatement("this.$N = $N", dictConfig.getDictValueField(), dictConfig.getDictValueField())
                        .build());
                dictDataMap.get(typeCode).stream().forEach(dictData -> {
                    Logger.LOG.info("dictData====>" + dictData);
                    enumTypeSpecBuilder.addEnumConstant(CommonUtil.toConstantLabel(dictData.getDictName()), TypeSpec.anonymousClassBuilder("$S,$S", dictData.getDictName(), dictData.getDictValue())
                            .build());
                });
                JavaFile javaFile = JavaFile.builder(dictConfig.getOutputPackName(), enumTypeSpecBuilder.build()).build();
                if (!outputDirectory.exists()) outputDirectory.mkdirs();
                javaFile.writeTo(outputDirectory);
            } catch (IOException e) {
                Logger.LOG.error(e);
                throw new RuntimeException(e);
            }
        });
        Logger.LOG.info("DictPlugin end......");
    }
}
