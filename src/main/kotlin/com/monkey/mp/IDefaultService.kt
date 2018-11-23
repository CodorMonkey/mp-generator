package com.monkey.mp

import com.baomidou.mybatisplus.plugins.Page

/**
 * 作者：Monkey
 * 日期：2017/10/23
 */
interface IDefaultService<T> {
    fun selectByEntity(entity: T): List<T>

    fun selectPageByEntity(page: Page<T>, entity: T): Page<T>
}