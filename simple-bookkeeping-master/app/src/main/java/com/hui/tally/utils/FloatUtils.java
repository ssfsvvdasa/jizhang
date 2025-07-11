package com.hui.tally.utils;

import java.math.BigDecimal;

// 浮点数工具类
public class FloatUtils {

    /* 除法运算（保留4位小数） */
    public static float div(float v1, float v2) {
        // 先进行基本除法运算
        float v3 = v1 / v2;
        // 使用BigDecimal进行精确小数处理
        BigDecimal b1 = new BigDecimal(v3);
        // 设置小数位数（4位）和舍入模式（ROUND_HALF_UP：四舍五入）
        float val = b1.setScale(4, 4).floatValue();
        return val;
    }

    /* 将浮点数转换为百分比形式 */
    public static String ratioToPercent(float val) {
        // 将小数转换为百分比值（乘以100）
        float v = val * 100;
        // 使用BigDecimal进行精确小数处理
        BigDecimal b1 = new BigDecimal(v);
        // 设置小数位数（2位）和舍入模式（ROUND_HALF_UP：四舍五入）
        float v1 = b1.setScale(2, 4).floatValue();
        // 添加百分号形成百分比字符串
        String per = v1 + "%";
        return per;
    }
}