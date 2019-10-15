package com.monkey.generator

import com.baomidou.mybatisplus.generator.config.ConstVal
import com.baomidou.mybatisplus.generator.config.po.TableField
import org.apache.commons.lang.StringUtils
import org.apache.ibatis.logging.LogFactory
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

/**
 * 作者：Monkey
 * 日期：2017/9/28
 */
object StatusGenerator {
    private val logger = LogFactory.getLog(this.javaClass)

    private var velocity: VelocityEngine = VelocityEngine()
    private val formatReg = Regex("_(\\w)")
    private val statusReg = Regex("^@(.+)")
    private val statusValueReg = Regex("(\\d+)[ ]*=[ ]*(\\w+)[ ]*((?:.|\\w)*)")

    init {
        velocity.setProperty(Velocity.RESOURCE_LOADER, "classpath")
        velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.qualifiedName)
    }

    fun generate(
        codePath: String,
        packageName: String,
        field: TableField,
        author: String = "") {
        val statusFind = statusReg.find(field.comment)

        val ctx = VelocityContext()
        if (statusFind != null) {
            val statusName = formatReg.replace(field.name) {
                it.groupValues[1].capitalize()
            }.capitalize()
            val statusComment = statusFind.groupValues[1]

            ctx.put("packageName", packageName)
            ctx.put("statusName", statusName)
            ctx.put("statusComment", statusComment)
            ctx.put("author", author)
            val values = statusValueReg.findAll(field.comment)
            val fields = values.map {
                val map = HashMap<String, String>()
                map["key"] = it.groupValues[2].toUpperCase()
                map["value"] = it.groupValues[1]
                map["comment"] = "/** ${it.groupValues[3]} **/"
                return@map map
            }
            ctx.put("statusFields", fields)
            val outputPath = "$codePath${File.separator}${packageName.replace(".", File.separator)}"
            this.vmToFile(ctx, "/templates/status.java.vm", "$outputPath${File.separator}$statusName.java")
        }
    }

    private fun vmToFile(context: VelocityContext, templatePath: String, outputFile: String) {
        if (!StringUtils.isEmpty(templatePath)) {
            val template = velocity.getTemplate(templatePath, ConstVal.UTF8)
            val file = File(outputFile)
            if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
                logger.debug("创建文件所在的目录失败!")
            } else {
                val fos = FileOutputStream(outputFile)
                val writer = BufferedWriter(OutputStreamWriter(fos, ConstVal.UTF8))
                template.merge(context, writer)
                writer.close()
                logger.debug("模板:$templatePath;  文件:$outputFile")
            }
        }
    }
}