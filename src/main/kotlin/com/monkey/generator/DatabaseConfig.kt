package com.monkey.generator

import com.baomidou.mybatisplus.annotation.DbType

/**
 * 作者：Monkey
 * 日期：2017/9/26
 */
data class DatabaseConfig(
    var dbType: DbType = DbType.MYSQL,
    var driver: String = "com.mysql.jdbc.Driver",
    var user: String,
    var pwd: String,
    var url: String,
    val schemaName: String? = null
)