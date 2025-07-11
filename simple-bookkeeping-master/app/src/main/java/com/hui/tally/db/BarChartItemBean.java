package com.hui.tally.db;

/*
 * 用于描述绘制柱状图时，每一个柱子表示的对象
 * 表示柱状图中单根柱子的数据模型（通常按日聚合）
 */
public class BarChartItemBean {
    // 字段定义
    int year;        // 记录年份（如2023）
    int month;       // 记录月份（1-12）
    int day;         // 记录日期（1-31）
    float summoney;  // 该日期的总金额（支出或收入汇总）

    // 构造方法

    /** 无参构造方法（用于ORM框架） */
    public BarChartItemBean() {
    }

    /**
     * 全参数构造方法（用于创建完整数据项）
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param summoney 当日总金额
     */
    public BarChartItemBean(int year, int month, int day, float summoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.summoney = summoney;
    }

    // Getter和Setter方法

    /** 获取年份 */
    public int getYear() {
        return year;
    }

    /** 设置年份 */
    public void setYear(int year) {
        this.year = year;
    }

    /** 获取月份 */
    public int getMonth() {
        return month;
    }

    /** 设置月份 */
    public void setMonth(int month) {
        this.month = month;
    }

    /** 获取日期 */
    public int getDay() {
        return day;
    }

    /** 设置日期 */
    public void setDay(int day) {
        this.day = day;
    }

    /** 获取当日总金额 */
    public float getSummoney() {
        return summoney;
    }

    /** 设置当日总金额 */
    public void setSummoney(float summoney) {
        this.summoney = summoney;
    }
}