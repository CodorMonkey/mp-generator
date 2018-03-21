package com.monkey.base.mp.impl

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.monkey.base.mp.IMyService

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
open class MyServiceImpl<M : BaseMapper<T>, T> : ServiceImpl<M, T>(), IMyService<T> {
    override fun selectByEntity(entity: T): List<T> {
        return this.selectList(EntityWrapper(entity))
    }

    override fun selectPageByEntity(page: Page<T>, entity: T): Page<T> {
        return this.selectPage(page, EntityWrapper(entity))
    }

}