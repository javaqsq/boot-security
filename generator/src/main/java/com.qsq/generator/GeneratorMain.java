package com.qsq.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.mysql.cj.jdbc.Driver;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author QSQ
 * @create 2019/9/27 10:17
 * No, again
 * 〈〉
 */
public class GeneratorMain {

    /**
     * 是否强制带上注解
     */
    private boolean enableTableFieldAnnotation = true;
    /**
     * 生成的注解带上IdType类型
     */
    private IdType tableIdType = null;
    /**
     * 是否去掉生成实体的属性名前缀
     */
    private String[] fieldPrefix = null;
    /**
     * 生成的Service 接口类名是否以I开头
     * <p>默认是以I开头</p>
     * <p>user表 -> IUserService, UserServiceImpl</p>
     */
    private boolean serviceClassNameStartWithI = false;

    @Test
    public void generateCode() {
        String packageName = CodeProperties.packageName;
        enableTableFieldAnnotation = false;
        tableIdType = null;
        generateByTables(packageName, CodeProperties.tableName);

    }

    private void generateByTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = CodeProperties.dbUrl;
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL)
                .setUrl(dbUrl)
                .setUsername(CodeProperties.dbUsername)
                .setPassword(CodeProperties.dbPassWord)
                .setDriverName(Driver.class.getName());
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true) // 设置实体类是否需要 lombok 属性
                .setRestControllerStyle(true)
                .setLogicDeleteFieldName(CodeProperties.LogicDeleteFieldName)
                // .setDbColumnUnderline(true) 改为如下 2 个配置
                .setNaming(NamingStrategy.underline_to_camel)
                .setColumnNaming(NamingStrategy.underline_to_camel)
                .setEntityTableFieldAnnotationEnable(enableTableFieldAnnotation)
                .setFieldPrefix(fieldPrefix)//test_id -> id, test_type -> type
                .setInclude(tableNames)// 修改替换成你需要的表名，多个表名传数组
                .setSuperControllerClass(CodeProperties.baseControllerClassName)
                .setSuperEntityClass(CodeProperties.superEntityClass)
                .setSuperEntityColumns(CodeProperties.superEntityColumns)
//                .setSuperServiceImplClass(CodeProperties.SuperServiceClass)
        ;
        config.setActiveRecord(false)
                .setIdType(tableIdType)
                .setAuthor("qsq")
                .setOutputDir("d:\\codeGen")
                .setFileOverride(true)
                .setDateType(DateType.ONLY_DATE)


        ;
        if (!serviceClassNameStartWithI) {
            config.setServiceName("%sService");
        }
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        TemplateConfig templateConfig = new TemplateConfig()
//                .setEntity(CodeProperties.templateEntity)
                .setController(CodeProperties.temPlateController);
        InjectionConfig injectionConfig = new InjectionConfig() {
            //自定义属性注入:abc
            //在.vm/ftl模板中，通过${cfg.abc}获取属性
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
                this.setMap(map);
            }
        };
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                //配置自定义模板
                .setTemplate(templateConfig)
                //配置自定义属性注入
                .setCfg(injectionConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setController("controller")
                                .setService("service")
                                .setServiceImpl("service.impl")
                                .setEntity("db.bean")
                                .setMapper("db.dao")
                                .setXml("db.dao")
                ).execute();
    }
}