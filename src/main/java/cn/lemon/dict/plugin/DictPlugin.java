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
import cn.lemon.dict.plugin.model.DbConnInfo;
import cn.lemon.dict.plugin.model.DictData;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
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

        DbConnInfo dbConnInfo = new DbConnInfo();
        dbConnInfo.setTypeCodeField("typeCode");
        dbConnInfo.setDictLabelField("dictName");
        dbConnInfo.setDictValueField("dictValue");
        dbConnInfo.setJdbcDriverClassName("com.mysql.cj.jdbc.Driver");
        dbConnInfo.setUrl("jdbc:mysql://localhost:3306");
        dbConnInfo.setUserName("root");
        dbConnInfo.setPwd("abc123");
        dbConnInfo.setDictSql("select type.TYPE_CODE as typeCode,dict.DICT_NAME as dictName,dict.DICT_VALUE as dictValue from dataassets_baseline.sym_dictionary_type_t type inner join dataassets_baseline.sym_dictionary_t dict on type.TID = dict.DICT_TYPE_ID");

        DbExecutor executor = new MysqlDbExecutor(dbConnInfo);
        Map<String, Set<DictData>> dictDataMap = executor.dictSearch();

        Logger.LOG.info("DictPlugin end......");
    }
}
