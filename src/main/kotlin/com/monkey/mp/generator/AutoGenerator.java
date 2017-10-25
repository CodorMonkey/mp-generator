package com.monkey.mp.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class AutoGenerator {
    private static final Log logger = LogFactory.getLog(AutoGenerator.class);
    protected ConfigBuilder config;
    protected InjectionConfig injectionConfig;
    private DataSourceConfig dataSource;
    private StrategyConfig strategy;
    private PackageConfig packageInfo;
    private TemplateConfig template;
    private GlobalConfig globalConfig;
    private VelocityEngine engine;

    public AutoGenerator() {
    }

    public void execute() {
        logger.debug("==========================准备生成文件...==========================" );
        this.initConfig();
        this.mkdirs(this.config.getPathInfo());
        Map<String, VelocityContext> ctxData = this.analyzeData(this.config);
        Iterator i$ = ctxData.entrySet().iterator();

        while (i$.hasNext()) {
            Entry<String, VelocityContext> ctx = (Entry) i$.next();
            this.batchOutput((String) ctx.getKey(), (VelocityContext) ctx.getValue());
        }

        if (this.config.getGlobalConfig().isOpen()) {
            try {
                String osName = System.getProperty("os.name" );
                if (osName != null) {
                    if (osName.contains("Mac" )) {
                        Runtime.getRuntime().exec("open " + this.config.getGlobalConfig().getOutputDir());
                    } else if (osName.contains("Windows" )) {
                        Runtime.getRuntime().exec("cmd /c start " + this.config.getGlobalConfig().getOutputDir());
                    } else {
                        logger.debug("文件输出目录:" + this.config.getGlobalConfig().getOutputDir());
                    }
                }
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }

        logger.debug("==========================文件生成完成！！！==========================" );
    }

    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        return config.getTableInfoList();
    }

    private Map<String, VelocityContext> analyzeData(ConfigBuilder config) {
        List<TableInfo> tableList = this.getAllTableInfoList(config);
        Map<String, String> packageInfo = config.getPackageInfo();
        Map<String, VelocityContext> ctxData = new HashMap();
        String superEntityClass = this.getSuperClassName(config.getSuperEntityClass());
        String superMapperClass = this.getSuperClassName(config.getSuperMapperClass());
        String superServiceClass = this.getSuperClassName(config.getSuperServiceClass());
        String superServiceImplClass = this.getSuperClassName(config.getSuperServiceImplClass());
        String superControllerClass = this.getSuperClassName(config.getSuperControllerClass());
        String date = (new SimpleDateFormat("yyyy-MM-dd" )).format(new Date());
        Iterator i$ = tableList.iterator();

        while (i$.hasNext()) {
            TableInfo tableInfo = (TableInfo) i$.next();
            VelocityContext ctx = new VelocityContext();
            if (null != this.injectionConfig) {
                this.injectionConfig.initMap();
                ctx.put("cfg" , this.injectionConfig.getMap());
            }

            if (config.getGlobalConfig().isActiveRecord()) {
                tableInfo.setImportPackages("com.baomidou.mybatisplus.activerecord.Model" );
            }

            if (tableInfo.isConvert()) {
                tableInfo.setImportPackages("com.baomidou.mybatisplus.annotations.TableName" );
            }

            if (tableInfo.isLogicDelete(config.getStrategyConfig().getLogicDeleteFieldName())) {
                tableInfo.setImportPackages("com.baomidou.mybatisplus.annotations.TableLogic" );
            }

            if (StringUtils.isNotEmpty(config.getStrategyConfig().getVersionFieldName())) {
                tableInfo.setImportPackages("com.baomidou.mybatisplus.annotations.Version" );
            }

            if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
                tableInfo.setImportPackages(config.getSuperEntityClass());
            } else {
                tableInfo.setImportPackages("java.io.Serializable" );
            }

            if (config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix()) {
                Iterator iterator = tableInfo.getFields().iterator();

                while (iterator.hasNext()) {
                    TableField field = (TableField) iterator.next();
                    if (field.getPropertyType().equalsIgnoreCase("boolean" ) && field.getPropertyName().startsWith("is" )) {
                        field.setPropertyName(config.getStrategyConfig(), StringUtils.removePrefixAfterPrefixToLower(field.getPropertyName(), 2));
                    }
                }
            }

            if (config.getStrategyConfig().isControllerMappingHyphenStyle()) {
                ctx.put("controllerMappingHyphenStyle" , config.getStrategyConfig().isControllerMappingHyphenStyle());
                ctx.put("controllerMappingHyphen" , StringUtils.camelToHyphen(tableInfo.getEntityPath()));
            }

            ctx.put("restControllerStyle" , config.getStrategyConfig().isRestControllerStyle());
            ctx.put("package" , packageInfo);
            ctx.put("author" , config.getGlobalConfig().getAuthor());
            ctx.put("logicDeleteFieldName" , config.getStrategyConfig().getLogicDeleteFieldName());
            ctx.put("versionFieldName" , config.getStrategyConfig().getVersionFieldName());
            ctx.put("activeRecord" , config.getGlobalConfig().isActiveRecord());
            ctx.put("date" , date);
            ctx.put("table" , tableInfo);
            ctx.put("enableCache" , config.getGlobalConfig().isEnableCache());
            ctx.put("baseResultMap" , config.getGlobalConfig().isBaseResultMap());
            ctx.put("baseColumnList" , config.getGlobalConfig().isBaseColumnList());
            ctx.put("entity" , tableInfo.getEntityName());
            ctx.put("entityColumnConstant" , config.getStrategyConfig().isEntityColumnConstant());
            ctx.put("entityBuilderModel" , config.getStrategyConfig().isEntityBuilderModel());
            ctx.put("entityLombokModel" , config.getStrategyConfig().isEntityLombokModel());
            ctx.put("entityBooleanColumnRemoveIsPrefix" , config.getStrategyConfig().isEntityBooleanColumnRemoveIsPrefix());
            ctx.put("superEntityClass" , superEntityClass);
            ctx.put("superMapperClassPackage" , config.getSuperMapperClass());
            ctx.put("superMapperClass" , superMapperClass);
            ctx.put("superServiceClassPackage" , config.getSuperServiceClass());
            ctx.put("superServiceClass" , superServiceClass);
            ctx.put("superServiceImplClassPackage" , config.getSuperServiceImplClass());
            ctx.put("superServiceImplClass" , superServiceImplClass);
            ctx.put("superControllerClassPackage" , config.getSuperControllerClass());
            ctx.put("superControllerClass" , superControllerClass);
            ctxData.put(tableInfo.getEntityName(), ctx);
        }

        return ctxData;
    }

    private String getSuperClassName(String classPath) {
        return StringUtils.isEmpty(classPath) ? null : classPath.substring(classPath.lastIndexOf("." ) + 1);
    }

    private void mkdirs(Map<String, String> pathInfo) {
        Iterator i$ = pathInfo.entrySet().iterator();

        while (i$.hasNext()) {
            Entry<String, String> entry = (Entry) i$.next();
            File dir = new File((String) entry.getValue());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + (String) entry.getValue() + "]" );
                }
            }
        }

    }

    private void batchOutput(String entityName, VelocityContext context) {
        try {
            TableInfo tableInfo = (TableInfo) context.get("table" );
            Map<String, String> pathInfo = this.config.getPathInfo();
            String entityFile = String.format(pathInfo.get("entity_path" ) + ConstVal.ENTITY_NAME, entityName);
            String mapperFile = String.format(pathInfo.get("mapper_path" ) + File.separator + tableInfo.getMapperName() + ".java" , entityName);
            String xmlFile = String.format(pathInfo.get("xml_path" ) + File.separator + tableInfo.getXmlName() + ".xml" , entityName);

            String templateService = template.getService();
            String serviceExt = templateService.split("\\." )[1];
            String serviceFile = String.format(pathInfo.get("serivce_path" ) + File.separator + tableInfo.getServiceName() + "." + serviceExt, entityName);

            String templateImpl = template.getServiceImpl();
            String implExt = templateImpl.split("\\." )[1];
            String implFile = String.format(pathInfo.get("serviceimpl_path" ) + File.separator + tableInfo.getServiceImplName() + "." + implExt, entityName);

            String controllerFile = String.format(pathInfo.get("controller_path" ) + File.separator + tableInfo.getControllerName() + ".java" , entityName);
            TemplateConfig template = this.config.getTemplate();
            if (this.isCreate(entityFile)) {
                this.vmToFile(context, template.getEntity(), entityFile);
            }

            if (this.isCreate(mapperFile)) {
                this.vmToFile(context, template.getMapper(), mapperFile);
            }

            if (this.isCreate(xmlFile)) {
                this.vmToFile(context, template.getXml(), xmlFile);
            }

            if (this.isCreate(serviceFile)) {
                this.vmToFile(context, templateService, serviceFile);
            }

            if (this.isCreate(implFile)) {
                this.vmToFile(context, template.getServiceImpl(), implFile);
            }

            if (this.isCreate(controllerFile)) {
                this.vmToFile(context, template.getController(), controllerFile);
            }

            if (this.injectionConfig != null) {
                List<FileOutConfig> focList = this.injectionConfig.getFileOutConfigList();
                if (CollectionUtils.isNotEmpty(focList)) {
                    Iterator i$ = focList.iterator();

                    while (i$.hasNext()) {
                        FileOutConfig foc = (FileOutConfig) i$.next();
                        this.vmToFile(context, foc.getTemplatePath(), foc.outputFile(tableInfo));
                    }
                }
            }
        } catch (IOException var15) {
            logger.error("无法创建文件，请检查配置信息！" , var15);
        }

    }

    private void vmToFile(VelocityContext context, String templatePath, String outputFile) throws IOException {
        if (!StringUtils.isEmpty(templatePath)) {
            VelocityEngine velocity = this.getVelocityEngine();
            Template template = velocity.getTemplate(templatePath, ConstVal.UTF8);
            File file = new File(outputFile);
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                logger.debug("创建文件所在的目录失败!" );
            } else {
                FileOutputStream fos = new FileOutputStream(outputFile);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, ConstVal.UTF8));
                template.merge(context, writer);
                writer.close();
                logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
            }
        }
    }

    private VelocityEngine getVelocityEngine() {
        if (this.engine == null) {
            Properties p = new Properties();
            p.setProperty("file.resource.loader.class" , "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
            p.setProperty("file.resource.loader.path" , "" );
            p.setProperty("ISO-8859-1" , ConstVal.UTF8);
            p.setProperty("input.encoding" , ConstVal.UTF8);
            p.setProperty("output.encoding" , ConstVal.UTF8);
            p.setProperty("file.resource.loader.unicode" , "true" );
            this.engine = new VelocityEngine(p);
        }

        return this.engine;
    }

    private boolean isCreate(String filePath) {
        File file = new File(filePath);
        return !file.exists() || this.config.getGlobalConfig().isFileOverride();
    }

    protected void initConfig() {
        if (null == this.config) {
            this.config = new ConfigBuilder(this.packageInfo, this.dataSource, this.strategy, this.template, this.globalConfig);
            if (null != this.injectionConfig) {
                this.injectionConfig.setConfig(this.config);
            }
        }

    }

    public DataSourceConfig getDataSource() {
        return this.dataSource;
    }

    public AutoGenerator setDataSource(DataSourceConfig dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public StrategyConfig getStrategy() {
        return this.strategy;
    }

    public AutoGenerator setStrategy(StrategyConfig strategy) {
        this.strategy = strategy;
        return this;
    }

    public PackageConfig getPackageInfo() {
        return this.packageInfo;
    }

    public AutoGenerator setPackageInfo(PackageConfig packageInfo) {
        this.packageInfo = packageInfo;
        return this;
    }

    public TemplateConfig getTemplate() {
        return this.template;
    }

    public AutoGenerator setTemplate(TemplateConfig template) {
        this.template = template;
        return this;
    }

    public ConfigBuilder getConfig() {
        return this.config;
    }

    public AutoGenerator setConfig(ConfigBuilder config) {
        this.config = config;
        return this;
    }

    public GlobalConfig getGlobalConfig() {
        return this.globalConfig;
    }

    public AutoGenerator setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public InjectionConfig getCfg() {
        return this.injectionConfig;
    }

    public AutoGenerator setCfg(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }
}