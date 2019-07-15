package com.monkey.projects

import com.baomidou.mybatisplus.annotation.DbType
import com.monkey.generator.DatabaseConfig
import com.monkey.generator.MpGenerator
import com.monkey.generator.ProgramingLanguage

/**
 * author：Monkey
 * date：2018/6/11
 */
fun main(args: Array<String>) {
    val author = "Monkey"

    val dbConfig = DatabaseConfig(
        driver = "org.postgresql.Driver",
        url = "jdbc:postgresql://localhost:5432/todo",
        user = "postgres",
        pwd = "root",
        dbType = DbType.POSTGRE_SQL
    )

    MpGenerator.generate(
        codePath = "D:\\study\\kotlin\\todo\\src\\main\\kotlin",
        xmlPath = "D:\\study\\kotlin\\todo\\src\\main\\resources\\mapper",
        basePackage = "com.monkey.todo",
        author = author,
        dbConfig = dbConfig,
        language = ProgramingLanguage.KOTLIN,
        initCore = true
    )
}