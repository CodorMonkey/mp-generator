package com.monkey

import com.monkey.mp.generator.StatusGenerator

/**
 * 作者：Monkey
 * 日期：2017/9/27
 */
fun main(args: Array<String>) {
    val str = """
        @verify_status 申请的状态
            1=  pending 待审批
            2  =approved 审批通过
            3= unapproved 审批不通过
            4 =canceled 用户撤销
    """

    val str2 = """
        @order_status 订单状态
0=new新下单(默认)
    """

    StatusGenerator.generate(
        codePath = "E:\\study\\kotlin\\mp\\src\\main\\kotlin",
        packageName = "com.monkey.status",
        str = str2,
        author = "Monkey"
    )
}