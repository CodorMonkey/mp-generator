package com.monkey.generator

import com.baomidou.mybatisplus.annotation.FieldFill
import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.generator.AutoGenerator
import com.baomidou.mybatisplus.generator.InjectionConfig
import com.baomidou.mybatisplus.generator.config.*
import com.baomidou.mybatisplus.generator.config.po.TableFill
import com.baomidou.mybatisplus.generator.config.po.TableInfo
import com.baomidou.mybatisplus.generator.config.rules.DateType
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy
import java.io.File
import java.util.*

/**
 * 作者：Monkey
 * 日期：2017/9/26
 */
object MpGenerator {
    fun generate(codePath: String,
                 xmlPath: String,
                 basePackage: String,
                 author: String = "",
                 dbConfig: DatabaseConfig,
                 arrInclude: Array<String> = arrayOf(),
                 arrexclude: Array<String> = arrayOf("schema_version", "flyway_schema_history"),
                 language: ProgramingLanguage = ProgramingLanguage.JAVA,
                 initCore: Boolean = false) {
        val codeAbsolutePath = "${codePath.trimEnd(File.separatorChar)}${File.separator}"
        val xmlAbsolutePath = "${xmlPath.trimEnd(File.separatorChar)}${File.separator}"

        // 自定义需要填充的字段
        val tableFillList = ArrayList<TableFill>()
        //        tableFillList.add(TableFill("create_time", FieldFill.INSERT))
        tableFillList.add(TableFill("update_time", FieldFill.UPDATE))

        var extName = "java"
        if (language == ProgramingLanguage.KOTLIN) {
            extName = "kt"
        }

        val bizPackage = "$basePackage.biz"
        // val mpPackage = "$basePackage.mp"

        // 策略配置
        val strategyConfig = StrategyConfig()
            // .setDbColumnUnderline(true)//全局下划线命名
            .setTablePrefix("T_") // 此处可以修改为您的表前缀
            .setNaming(NamingStrategy.underline_to_camel) // 表名生成策略
            //            .setColumnNaming(NamingStrategy.underline_to_camel)
            // 自定义实体，公共字段
            .setTableFillList(tableFillList)
            // 自定义 service 父类
//            .setSuperServiceClass("$mpPackage.IDefaultService")
            // 自定义 service 实现类父类
//            .setSuperServiceImplClass("$mpPackage.impl.DefaultServiceImpl")
            //                 .setEntityLombokModel(true)
            .setRestControllerStyle(true)
        // .setControllerMappingHyphenStyle(true)
        if (arrInclude.isNotEmpty()) {
            strategyConfig.setInclude(*arrInclude)  // 需要生成的表
        } else if (arrexclude.isNotEmpty()) {
            strategyConfig.setExclude(*arrexclude) // 排除生成的表
        }

        // 全局配置
        val globalConfig = GlobalConfig()
            .setOpen(false)
            .setOutputDir(codeAbsolutePath) //输出目录
            .setFileOverride(true) // 是否覆盖文件
            .setActiveRecord(true) // 开启 activeRecord 模式
            .setEnableCache(false) // XML 二级缓存
            .setBaseResultMap(true) // XML ResultMap
            .setBaseColumnList(true) // XML columList
            .setAuthor(author)
            .setKotlin(language == ProgramingLanguage.KOTLIN)
            // 自定义文件命名，注意 %s 会自动填充表实体属性！
            .setEntityName("%sPo")
//            .setDateType(DateType.SQL_PACK)
            .setIdType(IdType.AUTO)
            .setSwagger2(true)

        // 数据源配置
        val dataSourceConfig = DataSourceConfig()
            .setDbType(dbConfig.dbType) // 数据库类型
            .setDriverName(dbConfig.driver)
            .setUsername(dbConfig.user)
            .setPassword(dbConfig.pwd)
            .setUrl(dbConfig.url)
            .setSchemaName(dbConfig.schemaName)
            .setTypeConvert(PgTypeConverter())

        // 包配置
        val packageConfig = PackageConfig()
            .setParent(bizPackage) // 自定义包路径
            .setController("controller") // 这里是控制器包名，默认 web
            .setEntity("entity.po")    // 实体类包名

        // 代码生成器
        val mpg = AutoGenerator()
            .setGlobalConfig(globalConfig)
            .setDataSource(dataSourceConfig)
            .setStrategy(strategyConfig)
            .setPackageInfo(packageConfig)
            .setCfg(
                // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
                object : InjectionConfig() {
                    override fun initMap() {
                        this.map = CacheGenerator.generate(codePath, xmlAbsolutePath) as Map<String, Any>?
                    }
                }.setFileOutConfigList(mutableListOf<FileOutConfig>(object : FileOutConfig("/templates/mapper.xml.vm") {
                    // 自定义输出文件目录
                    override fun outputFile(tableInfo: TableInfo): String {
                        tableInfo.fields.forEach {
                            if (it.comment?.isNotBlank() == true) {
                                StatusGenerator.generate(
                                    codePath = codePath,
                                    packageName = "$bizPackage.constants",
                                    str = it.comment,
                                    author = author
                                )
                            }
                        }
                        return "$xmlAbsolutePath${tableInfo.xmlName}.xml"
                    }
                }))
            )
            .setTemplate(
                TemplateConfig()
                    // 关闭默认 xml 生成，调整生成 至 根目录
                    .setXml(null)
                    // 自定义模板配置，模板可以参考源码 /mybatis-plus/src/main/resources/template 使用 copy
                    // 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
                    .setController(null)
                    //                    .setEntity("/templates/entity.java.vm")
                    .setService(null)
                    .setServiceImpl(null)
                    .setMapper("/templates/mapper.$extName.vm")
//                    .setService("/templates/service.$extName.vm")
//                    .setServiceImpl("/templates/serviceImpl.$extName.vm")
            )

        // 执行生成
        mpg.execute()

        if (initCore) {
            PackageDuplicator(
                listOf("mp", "core"),
                codePath,
                basePackage).duplicate()
        }
    }
}