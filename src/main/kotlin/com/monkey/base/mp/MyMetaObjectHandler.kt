package com.monkey.base.mp

import com.baomidou.mybatisplus.mapper.MetaObjectHandler
import org.apache.ibatis.reflection.MetaObject

/**
 * 作者：Monkey
 * 日期：2017/9/20
 */
class MyMetaObjectHandler : MetaObjectHandler() {
    override fun insertFill(meta: MetaObject?) {
        setFieldValByName("createTime", null, meta)
        setFieldValByName("updateTime", null, meta)
    }

    override fun updateFill(meta: MetaObject?) {
        setFieldValByName("updateTime", null, meta)
    }
}