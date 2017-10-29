package com.monkey.base

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Min

/**
 * 作者：Monkey
 * 日期：2017/10/29
 */
open class PageDTO(
    @field:Min(1, message = "页数最小为1")
    var pageNum: Int = 1,
    @field:Range(min = 0, message = "显示条数最小为0")
    var pageSize: Int = 0
)