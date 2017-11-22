package com.monkey.projects

import com.monkey.mp.generator.DatabaseConfig
import com.monkey.mp.generator.MpGenerator
import com.monkey.mp.generator.ProgramingLanguage

/**
 * 作者：Monkey
 * 日期：2017/8/30
 */

fun main(args: Array<String>) {
    val author = "Monkey"

    val dbConfig = DatabaseConfig(
        user = "root",
        pwd = "root",
        url = "jdbc:mysql://localhost:3306/ebaotech?characterEncoding=utf8&allowMultiQueries=true&useSSL=false"
    )

    MpGenerator.generate(
        codePath = "E:\\yuecheng\\ebaotech\\ebaotech_api\\src\\main\\kotlin",
        xmlPath = "E:\\yuecheng\\ebaotech\\ebaotech_api\\src\\main\\resources\\mapper",
        packageName = "com.bjbz.ebaotech.business",
        author = author,
        dbConfig = dbConfig,
        language = ProgramingLanguage.KOTLIN
    )
}