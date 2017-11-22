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
        url = "jdbc:mysql://localhost:3306/onduty?characterEncoding=utf8&allowMultiQueries=true&useSSL=false"
    )

    MpGenerator.generate(
        codePath = "E:\\yuecheng\\onduty\\onduty\\src\\main\\kotlin",
        xmlPath = "E:\\yuecheng\\onduty\\onduty\\src\\main\\resources\\mapper",
        packageName = "com.bjbz.onduty.business",
        author = author,
        dbConfig = dbConfig,
        language = ProgramingLanguage.KOTLIN
    )
}