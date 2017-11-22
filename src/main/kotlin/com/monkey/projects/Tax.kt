package com.monkey.projects

import com.baomidou.mybatisplus.generator.config.rules.DbType
import com.monkey.mp.generator.DatabaseConfig
import com.monkey.mp.generator.MpGenerator
import com.monkey.mp.generator.ProgramingLanguage


/**
 * 作者：Monkey
 * 日期：2017/8/30
 */

fun main(args: Array<String>) {
    val author = "Monkey"

    val dbConfig = DatabaseConfig(
        dbType = DbType.ORACLE,
        driver = "oracle.jdbc.OracleDriver",
        user = "jinsan",
        pwd = "jinsan",
        url = "jdbc:oracle:thin:@//92.12.66.83:1521/ZSPT"
    )

    MpGenerator.generate(
        codePath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\kotlin",
        xmlPath = "E:\\yuecheng\\statetax\\taxserver\\src\\main\\resources\\mapper\\",
        packageName = "com.bjbz.statetax.generator",
        author = author,
        dbConfig = dbConfig,
        language = ProgramingLanguage.KOTLIN,
        arrInclude = arrayOf(
            // 原有更新
            "DM0_ZSRK_R",
            // 新添加的
            "dm2_sfzrd_r", "dm2_sbbs_r", "dm2_fpyj_r", "dm2_zssj_r", "dm2_kjjks_r",
            "dm2_djxx_xzhfy_y", "DM2_SB_YNSEFJ_R", "DM2_SB_YNSEFJ_R", "DM2_ZSRKFJ_R",
            "DM2_SB_YNSR_BSFWT_N", "DM2_SB_YNSR_BSFWT_R", "DM2_FP_FPLX")
    )
}