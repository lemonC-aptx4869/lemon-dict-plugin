package cn.lemon.dict.plugin;


import cn.lemon.dict.plugin.jdbc.DbExecutor;
import cn.lemon.dict.plugin.jdbc.JdbcExecutor;
import cn.lemon.dict.plugin.model.DictConfig;
import cn.lemon.dict.plugin.model.DictData;
import cn.lemon.dict.plugin.model.DictType;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.SneakyThrows;
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
 * @Description: maven自定义字典编译器
 * @Author: lemonC
 * @Date: 2024/4/30
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
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/", property = "targetDirectory")
    private File generateSourceDir;
    /**
     * @doc 项目
     */
    @Parameter(property = "project")
    private MavenProject project;
    /**
     * @doc 配置文件
     */
    @Parameter(defaultValue = "${project.basedir}/src/main/resources/lemon-dict/config.xml", required = true, readonly = true)
    private File xmlConfig;

    @SneakyThrows
    public void execute() {
        Logger.LOG.info("=================================resolver config ================================== ");
        DictConfig dictConfig = CommonUtil.parseDictXml(xmlConfig);
        Logger.LOG.info(dictConfig.toString());
        dictConfig.getDictConfigs().stream().forEach(dictConfigNode -> {
            //查询字典
            Logger.LOG.info("connecting database......");
            DbExecutor executor = new JdbcExecutor(dictConfigNode);
            Map<DictType, Set<DictData>> dictDataMap = executor.dictSearch();
            //生成代码
            Logger.LOG.info(" ================================== generate source ================================== ");
            dictDataMap.keySet().stream().forEach(dictType -> {
                TypeSpec.Builder enumTypeSpecBuilder = TypeSpec.enumBuilder(dictType.getTypeCode())
                        .addModifiers(Modifier.PUBLIC);
                enumTypeSpecBuilder.addField(String.class, "description", Modifier.PRIVATE);
                enumTypeSpecBuilder.addField(dictType.getJavaType(), "code", Modifier.PRIVATE);
                enumTypeSpecBuilder.addMethod(
                        MethodSpec.constructorBuilder()
                                .addParameter(String.class, "description")
                                .addParameter(dictType.getJavaType(), "code")
                                .addStatement("this.$N = $N", "description", "description")
                                .addStatement("this.$N = $N", "code", "code")
                                .build()
                );
                enumTypeSpecBuilder.addMethod(
                        MethodSpec.methodBuilder("getDescription")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(dictType.getJavaType())
                                .addStatement(" return this.description ")
                                .build()
                );
                Logger.LOG.info("dictType\t" + dictType);
                dictDataMap.get(dictType).stream().forEach(dictData -> {
                    Logger.LOG.info("dictData\t" + dictData);
                    enumTypeSpecBuilder.addEnumConstant(
                            CommonUtil.toConstantLabel(dictType.getTypeCode() + "_" + dictData.getDictName()),
                            TypeSpec.anonymousClassBuilder("$S,$S", dictData.getDictName(), dictData.getDictValue()).build()
                    );
                });
                if (!generateSourceDir.exists()) generateSourceDir.mkdirs();
                try {
                    JavaFile javaFile = JavaFile.builder(dictConfigNode.getOutputPackName(), enumTypeSpecBuilder.build()).build();
                    javaFile.writeTo(generateSourceDir);
                } catch (IOException e) {
                    Logger.LOG.error(e);
                }
            });
        });
    }
}
