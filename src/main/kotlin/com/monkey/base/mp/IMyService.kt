package com.monkey.base.mp

import com.baomidou.mybatisplus.mapper.Wrapper
import com.baomidou.mybatisplus.service.IService
import com.github.pagehelper.Page
import com.monkey.base.PageDTO

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
interface IMyService<T> : IService<T> {
    fun selectByEntity(entity: T): List<T>

    fun selectPageByEntity(entity: T, pageDto: PageDTO): Page<T>

    fun selectPageByWrapper(wrapper: Wrapper<T>, pageDto: PageDTO): Page<T>
}