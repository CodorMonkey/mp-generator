package com.monkey.mp.generator

import com.baomidou.mybatisplus.generator.config.rules.DbType

/**
 * 作者：Monkey
 * 日期：2017/9/26
 */
data class DatabaseConfig(
    var dbType: DbType = DbType.MYSQL,
    var driver: String = "com.mysql.jdbc.Driver",
    var user: String,
    var pwd: String,
    var url: String
)