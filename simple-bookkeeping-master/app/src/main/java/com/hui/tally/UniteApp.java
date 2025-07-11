package com.hui.tally;

import android.app.Application;

import com.hui.tally.db.DBManager;

/* 全局应用类（自定义Application）*/
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();  // 调用父类Application的onCreate方法
        // 初始化数据库（在应用启动时创建数据库）
        DBManager.initDB(getApplicationContext());
    }
}