package com.monkey.base.mp

import com.baomidou.mybatisplus.plugins.Page
import com.baomidou.mybatisplus.service.IService

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
interface IMyService<T> : IService<T> {
    fun selectByEntity(entity: T): List<T>

    fun selectPageByEntity(page: Page<T>, entity: T): Page<T>
}