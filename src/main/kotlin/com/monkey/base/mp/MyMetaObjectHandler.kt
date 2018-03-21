package com.monkey.base.mp

import com.baomidou.mybatisplus.mapper.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject
import java.util.*

/**
 * 作者：Monkey
 * 日期：2017/9/20
 */
class MyMetaObjectHandler : MetaObjectHandler() {
    override fun insertFill(meta: MetaObject?) {
        val now = Date()
        setFieldValByName("createTime", now, meta)
        setFieldValByName("updateTime", now, meta)
    }

    override fun updateFill(meta: MetaObject?) {
        val now = Date()
        setFieldValByName("updateTime", now, meta)
    }
}