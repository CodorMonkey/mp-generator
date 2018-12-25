package com.monkey.generator

import java.io.File
import java.nio.charset.Charset

/**
 * 作者：Monkey
 * 日期：2017/9/26
 */
object CacheGenerator {
    private val charset = Charset.forName("UTF-8")
    private val javaContentReg = Regex("//==== 自定义开始 ====([\\s\\S]*)//==== 自定义结束 ====")
    private val javaImportReg = Regex("import.*[;]?")
    private val xmlContentReg = Regex("<!-- 自定义开始 -->([\\s\\S]*)<!-- 自定义结束 -->")

    fun generate(codePath: String, xmlPath: String): HashMap<String, HashMap<String, CustomField>> {
        val map = HashMap<String, HashMap<String, CustomField>>()

        val codeFile = File(codePath)
        val codeFileTreeWalk = codeFile.walk()

        fun cacheCustom(file: File, module: String, isXml: Boolean = false) {
            if (isXml) {
                if (file.extension != "xml") {
                    return
                }
            } else {
                if (module != file.parentFile.name) {
                    return
                }
            }

            val name = file.nameWithoutExtension
            val text = file.readText(charset)
            val find = if (isXml) {
                xmlContentReg.find(text)
            } else {
                javaContentReg.find(text)
            }
            if (find != null && find.groupValues.size > 1) {
                var content = find.groupValues[1]
                // 排除 换行回车空格tab 的影响
                content = content.trim(*"\r\n \t".toCharArray())
                if (content != "") {
                    val serviceMap = map.getOrPut(module) { HashMap() }
                    val import = if (isXml) {
                        null
                    } else {
                        javaImportReg.findAll(text).joinToString(separator = "\r\n") { it.value }
                    }
                    serviceMap[name] = CustomField(import, content)
                }
            }
        }

        codeFileTreeWalk.forEach {
            if (it.isFile) {
                cacheCustom(it, "mapper")
                cacheCustom(it, "service")
                cacheCustom(it, "impl")
            }
        }

        val xmlFile = File(xmlPath)
        val xmlFileTreeWalk = xmlFile.walk()
        xmlFileTreeWalk.forEach {
            if (it.isFile) {
                cacheCustom(it, "xml", true)
            }
        }

        return map
    }
}