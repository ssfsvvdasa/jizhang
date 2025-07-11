package com.hui.tally.db;

/**
 * 描述记录一条数据的相关内容类
 * 表示单笔记账记录的实体模型
 */
public class AccountBean {
    // 字段定义
    int id;              // 记录唯一标识（数据库主键）
    String typename;     // 收支类型名称（如"餐饮"、"交通"）
    int sImageId;        // 类型对应的图标资源ID
    String beizhu;       // 用户填写的备注信息
    float money;         // 金额数值（正数）
    String time;         // 完整时间字符串（格式如"2023-08-15 14:30"）
    int year;            // 记录年份（如2023）
    int month;           // 记录月份（1-12）
    int day;             // 记录日期（1-31）
    int kind;            // 收支类型标识：收入---1   支出---0

    // Getter和Setter方法

    /** 获取记录ID */
    public int getId() {
        return id;
    }

    /** 设置记录ID */
    public void setId(int id) {
        this.id = id;
    }

    /** 获取类型名称（如"餐饮"） */
    public String getTypename() {
        return typename;
    }

    /** 设置类型名称 */
    public void setTypename(String typename) {
        this.typename = typename;
    }

    /** 获取类型图标资源ID（用于显示在界面） */
    public int getsImageId() {
        return sImageId;
    }

    /** 设置类型图标资源ID */
    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    /** 获取备注信息 */
    public String getBeizhu() {
        return beizhu;
    }

    /** 设置备注信息 */
    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    /** 获取金额数值 */
    public float getMoney() {
        return money;
    }

    /** 设置金额数值 */
    public void setMoney(float money) {
        this.money = money;
    }

    /** 获取完整时间字符串 */
    public String getTime() {
        return time;
    }

    /** 设置完整时间字符串 */
    public void setTime(String time) {
        this.time = time;
    }

    /** 获取记录年份 */
    public int getYear() {
        return year;
    }

    /** 设置记录年份 */
    public void setYear(int year) {
        this.year = year;
    }

    /** 获取记录月份（1-12） */
    public int getMonth() {
        return month;
    }

    /** 设置记录月份 */
    public void setMonth(int month) {
        this.month = month;
    }

    /** 获取记录日期（1-31） */
    public int getDay() {
        return day;
    }

    /** 设置记录日期 */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 获取收支类型标识
     * @return 0-支出, 1-收入
     */
    public int getKind() {
        return kind;
    }

    /**
     * 设置收支类型标识
     * @param kind 0-支出, 1-收入
     */
    public void setKind(int kind) {
        this.kind = kind;
    }

    // 构造方法

    /** 无参构造方法（用于ORM框架） */
    public AccountBean() {
    }

    /**
     * 全参数构造方法（用于创建完整记录）
     * @param id 记录ID
     * @param typename 类型名称
     * @param sImageId 类型图标ID
     * @param beizhu 备注信息
     * @param money 金额
     * @param time 完整时间字符串
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param kind 收支类型（0-支出，1-收入）
     */
    public AccountBean(int id, String typename, int sImageId, String beizhu, float money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.typename = typename;
        this.sImageId = sImageId;
        this.beizhu = beizhu;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }
}