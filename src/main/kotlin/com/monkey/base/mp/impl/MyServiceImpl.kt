package com.monkey.base.mp.impl

import com.baomidou.mybatisplus.mapper.BaseMapper
import com.baomidou.mybatisplus.mapper.EntityWrapper
import com.baomidou.mybatisplus.mapper.Wrapper
import com.baomidou.mybatisplus.service.impl.ServiceImpl
import com.github.pagehelper.Page
import com.github.pagehelper.PageHelper
import com.monkey.base.mp.IMyService

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
open class MyServiceImpl<M : BaseMapper<T>, T> : ServiceImpl<M, T>(), IMyService<T> {
    override fun selectByEntity(entity: T): List<T> {
        return this.selectList(EntityWrapper(entity))
    }

    override fun selectPageByEntity(entity: T, pageDto: PageDTO): Page<T> {
        return this.selectPageByWrapper(EntityWrapper(entity), pageDto)
    }

    override fun selectPageByWrapper(wrapper: Wrapper<T>, pageDto: PageDTO): Page<T> {
        val page = PageHelper.startPage<T>(pageDto.pageNum, pageDto.pageSize)
        this.selectList(wrapper)
        PageHelper.clearPage()
        return page
    }

}