package com.hui.tally.db;

/*
 * 收支分类数据模型类
 * 用于表示收入或支出具体类型的数据结构
 * */
public class TypeBean {
    int id;             // 分类ID（数据库主键）
    String typename;   // 分类名称（如"餐饮"、"薪资"）
    int imageId;       // 未选中状态图标资源ID
    int simageId;      // 选中状态图标资源ID
    int kind;          // 收支类型标识（0=支出，1=收入）

    // Getter和Setter方法
    public int getId() {
        return id;  // 获取分类ID
    }

    public void setId(int id) {
        this.id = id;  // 设置分类ID
    }

    public String getTypename() {
        return typename;  // 获取分类名称
    }

    public void setTypename(String typename) {
        this.typename = typename;  // 设置分类名称
    }

    public int getImageId() {
        return imageId;  // 获取未选中图标ID
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;  // 设置未选中图标ID
    }

    public int getSimageId() {
        return simageId;  // 获取选中状态图标ID
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;  // 设置选中状态图标ID
    }

    public int getKind() {
        return kind;  // 获取收支类型（0/1）
    }

    public void setKind(int kind) {
        this.kind = kind;  // 设置收支类型（0/1）
    }

    // 无参构造函数
    public TypeBean() {
    }

    // 全参构造函数
    public TypeBean(int id, String typename, int imageId, int simageId, int kind) {
        this.id = id;          // 初始化分类ID
        this.typename = typename;  // 初始化分类名称
        this.imageId = imageId;    // 初始化未选中图标ID
        this.simageId = simageId;   // 初始化选中状态图标ID
        this.kind = kind;           // 初始化收支类型
    }
}