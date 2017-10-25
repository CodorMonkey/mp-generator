package com.monkey.projects

import com.baomidou.mybatisplus.generator.config.rules.DbType
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
        dbType = DbType.ORACLE,
        driver = "oracle.jdbc.OracleDriver",
        user = "ADP",
        pwd = "123qweasd",
        url = "jdbc:oracle:thin:@//192.168.1.158:1521/orcl"
    )

    MpGenerator.generate(
        codePath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\kotlin",
        xmlPath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\resources\\mapper\\",
        packageName = "com.bjbz.statetax.generator",
        author = author,
        dbConfig = dbConfig,
        language = ProgramingLanguage.KOTLIN
    )
}