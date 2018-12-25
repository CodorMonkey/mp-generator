package com.monkey.generator

import com.baomidou.mybatisplus.generator.config.ConstVal
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
    private val statusReg = Regex("@([a-zA-Z]\\w*)[ ]*(.*)")
    private val statusValueReg = Regex("(\\d+)[ ]*=[ ]*(\\w+)[ ]*((?:.|\\w)*)")

    init {
        velocity.setProperty(Velocity.RESOURCE_LOADER, "classpath")
        velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.qualifiedName)
    }

    fun generate(codePath: String,
                 packageName: String,
                 str: String,
                 author: String = "") {
        val statusFind = statusReg.find(str)

        val ctx = VelocityContext()
        if (statusFind != null && statusFind.groupValues.size > 1) {
            val statusName = formatReg.replace(statusFind.groupValues[1]) {
                it.groupValues[1].capitalize()
            }.capitalize()
            val statusComment = statusFind.groupValues[2]

            ctx.put("packageName", packageName)
            ctx.put("statusName", statusName)
            ctx.put("statusComment", statusComment)
            ctx.put("author", author)
            val values = statusValueReg.findAll(str)
            val fields = values.map {
                val map = HashMap<String, String>()
                map.put("key", it.groupValues[2].toUpperCase())
                map.put("value", it.groupValues[1])
                map.put("comment", "/** ${it.groupValues[3]} **/")
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