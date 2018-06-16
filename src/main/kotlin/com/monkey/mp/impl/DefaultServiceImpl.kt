package com.monkey.mp.impl

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.impl.ServiceImpl

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
open class DefaultServiceImpl<M : BaseMapper<T>, T> : ServiceImpl<M, T>() {
    protected fun selectByEntity(entity: T): List<T> {
        return this.selectList(EntityWrapper(entity))
    }

    protected fun selectPageByEntity(page: Page<T>, entity: T): Page<T> {
        return this.selectPage(page, EntityWrapper(entity))
    }

}