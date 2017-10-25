package com.monkey

import com.monkey.mp.generator.CacheGenerator

/**
 * 作者：Monkey
 * 日期：2017/9/26
 */
fun main(args: Array<String>) {
    val codePath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\kotlin"
    val xmlPath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\resources\\mapper\\"

    val map = CacheGenerator.generate(codePath, xmlPath)
    println(map)
}