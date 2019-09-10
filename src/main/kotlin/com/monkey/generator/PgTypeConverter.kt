package com.monkey.generator

import com.baomidou.mybatisplus.generator.config.GlobalConfig
import com.baomidou.mybatisplus.generator.config.ITypeConvert
import com.baomidou.mybatisplus.generator.config.rules.DateType
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType
import com.baomidou.mybatisplus.generator.config.rules.IColumnType

/**
 * author：Monkey
 * date：2019/9/9
 */
class PgTypeConverter : ITypeConvert {
    override fun processTypeConvert(globalConfig: GlobalConfig, fieldType: String): IColumnType {
        val t = fieldType.toLowerCase()
        when {
            t.contains("char")                       -> return DbColumnType.STRING
            t.contains("bigint")                     -> return DbColumnType.LONG
            t.contains("int")                        -> return DbColumnType.INTEGER
            t.contains("date") || t.contains("time") -> return when (globalConfig.dateType) {
                DateType.ONLY_DATE -> DbColumnType.DATE
                DateType.SQL_PACK  -> when (t) {
                    "date" -> DbColumnType.DATE_SQL
                    "time" -> DbColumnType.TIME
                    else   -> DbColumnType.TIMESTAMP
                }
                DateType.TIME_PACK -> when (t) {
                    "date" -> DbColumnType.LOCAL_DATE
                    "time" -> DbColumnType.LOCAL_TIME
                    else   -> DbColumnType.LOCAL_DATE_TIME
                }
                else               -> DbColumnType.DATE
            }
            t.contains("text")                       -> return DbColumnType.STRING
            t.contains("bit")                        -> return DbColumnType.BOOLEAN
            t.contains("decimal") || t.contains("numeric")
                                                     -> return DbColumnType.BIG_DECIMAL
            t.contains("clob")                       -> return DbColumnType.CLOB
            t.contains("blob")                       -> return DbColumnType.BYTE_ARRAY
            t.contains("float")                      -> return DbColumnType.FLOAT
            t.contains("double")                     -> return DbColumnType.DOUBLE
            t.contains("json") || t.contains("enum") -> return DbColumnType.STRING
            t.contains("boolean")                    -> return DbColumnType.BOOLEAN
            else                                     -> return DbColumnType.STRING
        }
    }
}