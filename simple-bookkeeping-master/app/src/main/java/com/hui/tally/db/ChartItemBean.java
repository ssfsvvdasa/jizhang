package com.hui.tally.db;

// 定义图表数据项的JavaBean类
public class ChartItemBean {
    int sImageId;         // 存储图标资源ID（如R.drawable.xxx）
    String type;          // 存储消费/收入类型（如"餐饮"、"购物"）
    float ratio;          // 存储该类型在图表中的占比（百分比值）
    float totalMoney;     // 存储该类型对应的总金额

    // 无参构造函数
    public ChartItemBean() {
    }

    // 以下是一组setter方法
    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;  // 设置图标资源ID
    }

    public void setType(String type) {
        this.type = type;           // 设置类型名称
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;        // 设置占比值
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney; // 设置总金额
    }

    // 以下是一组getter方法
    public int getsImageId() {
        return sImageId;  // 获取图标资源ID
    }

    public String getType() {
        return type;      // 获取类型名称
    }

    public float getRatio() {
        return ratio;     // 获取占比值
    }

    public float getTotalMoney() {
        return totalMoney; // 获取总金额
    }

    // 全参数构造函数（用于快速初始化对象）
    public ChartItemBean(int sImageId, String type, float ratio, float totalMoney) {
        this.sImageId = sImageId;   // 初始化图标ID
        this.type = type;           // 初始化类型
        this.ratio = ratio;         // 初始化占比
        this.totalMoney = totalMoney; // 初始化总金额
    }
}