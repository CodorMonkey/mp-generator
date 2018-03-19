package com.monkey.mp.generator.engine

import com.baomidou.mybatisplus.generator.config.ConstVal
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import com.baomidou.mybatisplus.toolkit.CollectionUtils
import java.io.File


/**
 * 作者：Monkey
 * 日期：2018/3/14
 */
class MyVelocityEngine : VelocityTemplateEngine() {
    override fun batchOutput(): AbstractTemplateEngine {
        try {
            val tableInfoList = this.configBuilder.tableInfoList
            for (tableInfo in tableInfoList) {
                val objectMap = this.getObjectMap(tableInfo)
                val pathInfo = this.configBuilder.pathInfo
                val template = this.configBuilder.template
                // 自定义内容
                val injectionConfig = this.configBuilder.injectionConfig
                if (null != injectionConfig) {
                    injectionConfig.initMap()
                    objectMap["cfg"] = injectionConfig.map
                    val focList = injectionConfig.fileOutConfigList
                    if (CollectionUtils.isNotEmpty(focList)) {
                        for (foc in focList) {
                            if (this.isCreate(foc.outputFile(tableInfo))) {
                                this.writer(objectMap, foc.templatePath, foc.outputFile(tableInfo))
                            }
                        }
                    }
                }
                // Mp.java
                val entityName = tableInfo.entityName
                if (null != entityName) {
                    val entityFile = String.format(pathInfo[ConstVal.ENTITY_PATH] + File.separator + "%s" + this.suffixJavaOrKt(), entityName)
                    if (this.isCreate(entityFile)) {
                        this.writer(objectMap, this.templateFilePath(template.getEntity(this.configBuilder.globalConfig.isKotlin)), entityFile)
                    }
                }
                // MpMapper.java
                if (null != tableInfo.mapperName) {
                    val mapperFile = String.format(pathInfo[ConstVal.MAPPER_PATH] + File.separator + tableInfo.mapperName + this.suffixJavaOrKt(), entityName)
                    if (this.isCreate(mapperFile)) {
                        this.writer(objectMap, this.templateFilePath(template.mapper), mapperFile)
                    }
                }
                // MpMapper.xml
                if (null != tableInfo.xmlName) {
                    val xmlFile = String.format(pathInfo[ConstVal.XML_PATH] + File.separator + tableInfo.xmlName + ConstVal.XML_SUFFIX, entityName)
                    if (this.isCreate(xmlFile)) {
                        this.writer(objectMap, this.templateFilePath(template.xml), xmlFile)
                    }
                }
                // IMpService.java
                if (null != tableInfo.serviceName) {
                    val serviceFile = String.format(pathInfo[ConstVal.SERIVCE_PATH] + File.separator + tableInfo.serviceName + this.suffixJavaOrKt(), entityName)
                    if (this.isCreate(serviceFile)) {
                        this.writer(objectMap, this.templateFilePath(template.service), serviceFile)
                    }
                }
                // MpServiceImpl.java
                if (null != tableInfo.serviceImplName) {
                    val implFile = String.format(pathInfo[ConstVal.SERVICEIMPL_PATH] + File.separator + tableInfo.serviceImplName + this.suffixJavaOrKt(), entityName)
                    if (this.isCreate(implFile)) {
                        this.writer(objectMap, this.templateFilePath(template.serviceImpl), implFile)
                    }
                }
                // MpController.java
                if (null != tableInfo.controllerName) {
                    val controllerFile = String.format(pathInfo[ConstVal.CONTROLLER_PATH] + File.separator + tableInfo.controllerName + this.suffixJavaOrKt(), entityName)
                    if (this.isCreate(controllerFile)) {
                        this.writer(objectMap, this.templateFilePath(template.controller), controllerFile)
                    }
                }
            }
        } catch (e: Exception) {
            AbstractTemplateEngine.logger.error("无法创建文件，请检查配置信息！", e)
        }

        return this
    }
}