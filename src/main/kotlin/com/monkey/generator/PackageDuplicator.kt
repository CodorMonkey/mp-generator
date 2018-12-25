package com.monkey.generator

import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine
import java.io.File

/**
 * author：Monkey
 * date：2018/11/24
 */
class PackageDuplicator(private val dirList: List<String>, val codePath: String, val basePackage: String) {
    private val baseTemplatePath = "templates"
    private val classpath = ClassLoader.getSystemResource("").path
    private val separator = File.separator

    private var engine: VelocityTemplateEngine = VelocityTemplateEngine()
    private var params: Map<String, String> = mapOf(Pair("basePackage", basePackage))

    init {
        engine.init(null)
    }


    fun duplicate() {
        dirList.forEach { dir ->
            val absoluteDirPath = "$classpath$baseTemplatePath$separator$dir"

            val file = File(absoluteDirPath)
            file.walk().forEach {
                if (it.isFile) {
                    val relativePath = it.absolutePath.split(baseTemplatePath)[1]
                    val templatePath = "$separator$baseTemplatePath$relativePath"
                    val distPath = "$codePath$separator${package2Path(basePackage)}${relativePath.trimEnd(*".vm".toCharArray())}"

                    val distFile = File(distPath)
                    if (!distFile.exists()) {
                        distFile.parentFile.mkdirs()
                    }
                    engine.writer(params, templatePath, distPath)
                }
            }
        }
    }

    private fun package2Path(pakage: String): String {
        return pakage.replace(".", separator)
    }
}

fun main(args: Array<String>) {
    PackageDuplicator(
        listOf("mp", "core"),
        "D:\\study\\kotlin\\todo\\src\\main\\kotlin",
        "com.monkey.todo").duplicate()
}