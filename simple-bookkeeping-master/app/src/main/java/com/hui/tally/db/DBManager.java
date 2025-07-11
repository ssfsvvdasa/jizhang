package com.hui.tally.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hui.tally.utils.FloatUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/*
 * 数据库管理工具类
 *   负责对数据库表进行增删改查操作
 * */
public class DBManager {

    private static SQLiteDatabase db;  // 静态数据库对象，全局共享

    /* 初始化数据库连接 */
    public static void initDB(Context context){
        DBOpenHelper helper = new DBOpenHelper(context);  // 创建数据库帮助类实例
        db = helper.getWritableDatabase();      // 获取可写数据库对象
    }

    /**
     * 根据收支类型获取分类数据
     * @param kind 收入(1)或支出(0)类型
     * @return 分类数据列表
     */
    public static List<TypeBean>getTypeList(int kind){
        List<TypeBean>list = new ArrayList<>();  // 创建空列表
        // 查询指定类型的分类数据
        String sql = "select * from typetb where kind = "+kind;
        Cursor cursor = db.rawQuery(sql, null);  // 执行SQL查询
        // 遍历查询结果
        while (cursor.moveToNext()) {
            // 从游标中提取字段值
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            // 创建分类对象并添加到列表
            TypeBean typeBean = new TypeBean(id, typename, imageId, sImageId, kind);
            list.add(typeBean);
        }
        return list;  // 返回结果列表
    }

    /*
     * 插入记账记录到数据库
     * @param bean 记账数据对象
     */
    public static void insertItemToAccounttb(AccountBean bean){
        ContentValues values = new ContentValues();  // 创建键值对容器
        // 设置字段值
        values.put("typename",bean.getTypename());
        values.put("sImageId",bean.getsImageId());
        values.put("beizhu",bean.getBeizhu());
        values.put("money",bean.getMoney());
        values.put("time",bean.getTime());
        values.put("year",bean.getYear());
        values.put("month",bean.getMonth());
        values.put("day",bean.getDay());
        values.put("kind",bean.getKind());
        db.insert("accounttb",null,values);  // 执行插入操作
    }

    /*
     * 获取某一天的收支记录
     * @param year 年
     * @param month 月
     * @param day 日
     * @return 当天的记账记录列表
     */
    public static List<AccountBean>getAccountListOneDayFromAccounttb(int year,int month,int day){
        List<AccountBean>list = new ArrayList<>();
        // 按日期查询，结果按ID降序排列
        String sql = "select * from accounttb where year=? and month=? and day=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        // 遍历查询结果
        while (cursor.moveToNext()) {
            // 提取字段值
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            // 创建记账对象并添加到列表
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /*
     * 获取某月的收支记录
     * @param year 年
     * @param month 月
     * @return 当月的记账记录列表
     */
    public static List<AccountBean>getAccountListOneMonthFromAccounttb(int year,int month){
        List<AccountBean>list = new ArrayList<>();
        // 按月查询，结果按ID降序排列
        String sql = "select * from accounttb where year=? and month=? order by id desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + ""});
        // 遍历查询结果
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, beizhu, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 获取某天收支总额
     * @param kind 收入(1)或支出(0)类型
     * @return 当日收支总额
     */
    public static float getSumMoneyOneDay(int year,int month,int day,int kind){
        float total = 0.0f;
        // 计算指定日期的收支总和
        String sql = "select sum(money) from accounttb where year=? and month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", day + "", kind + ""});
        // 获取结果
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
     * 获取某月收支总额
     * @param kind 收入(1)或支出(0)类型
     * @return 当月收支总额
     */
    public static float getSumMoneyOneMonth(int year,int month,int kind){
        float total = 0.0f;
        // 计算指定月份的收支总和
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /**
     * 统计某月收支记录数量
     * @param kind 收入(1)或支出(0)类型
     * @return 记录数量
     */
    public static int getCountItemOneMonth(int year,int month,int kind){
        int total = 0;
        // 计算指定月份的记录数
        String sql = "select count(money) from accounttb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));
            total = count;
        }
        return total;
    }

    /**
     * 获取某年收支总额
     * @param kind 收入(1)或支出(0)类型
     * @return 当年收支总额
     */
    public static float getSumMoneyOneYear(int year,int kind){
        float total = 0.0f;
        // 计算指定年份的收支总和
        String sql = "select sum(money) from accounttb where year=? and kind=?";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        return total;
    }

    /*
     * 根据ID删除记账记录
     * @param id 记录ID
     * @return 受影响的行数
     */
    public static int deleteItemFromAccounttbById(int id){
        int i = db.delete("accounttb", "id=?", new String[]{id + ""});
        return i;
    }

    /**
     * 根据备注模糊搜索记账记录
     * @param beizhu 备注关键词
     * @return 匹配的记账记录列表
     */
    public static List<AccountBean>getAccountListByRemarkFromAccounttb(String beizhu){
        List<AccountBean>list = new ArrayList<>();
        // 使用LIKE进行模糊查询
        String sql = "select * from accounttb where beizhu like '%"+beizhu+"%'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            String bz = cursor.getString(cursor.getColumnIndex("beizhu"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            AccountBean accountBean = new AccountBean(id, typename, sImageId, bz, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        return list;
    }

    /**
     * 获取数据库中的不重复年份列表
     * @return 年份列表（升序排列）
     */
    public static List<Integer>getYearListFromAccounttb(){
        List<Integer>list = new ArrayList<>();
        // 查询不重复年份并按升序排列
        String sql = "select distinct(year) from accounttb order by year asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        return list;
    }

    /*
     * 清空记账表
     */
    public static void deleteAllAccount(){
        String sql = "delete from accounttb";  // 删除所有记录
        db.execSQL(sql);  // 执行SQL语句
    }

    /**
     * 获取图表数据（按分类汇总）
     * @param kind 收入(1)或支出(0)类型
     * @return 图表数据项列表
     */
    public static List<ChartItemBean>getChartListFromAccounttb(int year,int month,int kind){
        List<ChartItemBean>list = new ArrayList<>();
        // 获取当月收支总额
        float sumMoneyOneMonth = getSumMoneyOneMonth(year, month, kind);
        // 按分类分组统计
        String sql = "select typename,sImageId,sum(money)as total from accounttb where year=? and month=? and kind=? group by typename " +
                "order by total desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float total = cursor.getFloat(cursor.getColumnIndex("total"));
            // 计算占比
            float ratio = FloatUtils.div(total,sumMoneyOneMonth);
            // 创建图表数据项
            ChartItemBean bean = new ChartItemBean(sImageId, typename, ratio, total);
            list.add(bean);
        }
        return list;
    }

    /**
     * 获取月内单日最大收支额
     * @return 最大金额值
     */
    public static float getMaxMoneyOneDayInMonth(int year,int month,int kind){
        // 按日分组并排序获取最大值
        String sql = "select sum(money) from accounttb where year=? and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            float money = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            return money;
        }
        return 0;
    }

    /**
     * 获取月内每日收支总额
     * @return 每日数据列表
     */
    public static List<BarChartItemBean>getSumMoneyOneDayInMonth(int year,int month,int kind){
        // 按日分组统计
        String sql = "select day,sum(money) from accounttb where year=? and month=? and kind=? group by day";
        Cursor cursor = db.rawQuery(sql, new String[]{year + "", month + "", kind + ""});
        List<BarChartItemBean>list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float smoney = cursor.getFloat(cursor.getColumnIndex("sum(money)"));
            // 创建柱状图数据项
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, smoney);
            list.add(itemBean);
        }
        return list;
    }

}