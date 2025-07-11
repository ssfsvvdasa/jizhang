package com.hui.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui.tally.R;
import com.hui.tally.db.AccountBean;
import com.hui.tally.db.DBManager;
import com.hui.tally.db.TypeBean;
import com.hui.tally.utils.BeiZhuDialog;
import com.hui.tally.utils.KeyBoardUtils;
import com.hui.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 记录页面的基类Fragment（抽象类）
 * 处理支出和收入记录的公共功能
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;  // 自定义键盘视图
    EditText moneyEt;           // 金额输入框
    ImageView typeIv;           // 类型图标
    TextView typeTv, beizhuTv, timeTv; // 类型、备注、时间文本
    GridView typeGv;            // 类型选择网格视图
    List<TypeBean> typeList;    // 类型数据列表
    TypeBaseAdapter adapter;    // 类型网格适配器
    AccountBean accountBean;    // 记账数据对象

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化记账数据对象
        accountBean = new AccountBean();
        // 设置默认类型
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 加载布局
        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        // 初始化视图
        initView(view);
        // 设置初始时间
        setInitTime();
        // 加载类型数据到网格视图
        loadDataToGV();
        // 设置网格项点击监听
        setGVListener();
        return view;
    }

    /* 设置初始时间到界面和记账对象 */
    private void setInitTime() {
        // 获取当前时间
        Date date = new Date();
        // 设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        // 显示时间
        timeTv.setText(time);
        // 保存到记账对象
        accountBean.setTime(time);

        // 获取日历实例
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需要+1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // 保存年月日到记账对象
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /* 设置类型网格的点击事件 */
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 更新选中位置
                adapter.selectPos = position;
                adapter.notifyDataSetInvalidated();  // 刷新网格视图

                // 获取选中的类型
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                // 更新类型文本
                typeTv.setText(typename);
                // 保存类型名称到记账对象
                accountBean.setTypename(typename);

                // 获取并显示类型图标
                int simageId = typeBean.getSimageId();
                typeIv.setImageResource(simageId);
                // 保存图标ID到记账对象
                accountBean.setsImageId(simageId);
            }
        });
    }

    /* 加载类型数据到网格视图 */
    public void loadDataToGV() {
        // 初始化类型列表和适配器
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    /* 初始化视图组件 */
    private void initView(View view) {
        // 绑定视图组件
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);

        // 设置点击监听
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        // 初始化自定义键盘
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard(); // 显示键盘

        // 设置键盘确定按钮监听
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                // 获取输入的金额
                String moneyStr = moneyEt.getText().toString();
                // 检查金额是否有效
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    getActivity().finish(); // 无效则关闭页面
                    return;
                }
                // 转换金额为浮点数
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money); // 保存金额

                // 保存记录到数据库（由子类实现）
                saveAccountToDB();
                // 关闭当前页面
                getActivity().finish();
            }
        });
    }

    /* 抽象方法：保存记录到数据库（子类必须实现） */
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View v) {
        // 处理点击事件
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog(); // 显示时间选择对话框
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog(); // 显示备注对话框
                break;
        }
    }

    /* 显示时间选择对话框 */
    private void showTimeDialog() {
        // 创建并显示时间选择对话框
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();

        // 设置确定按钮监听
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                // 更新时间显示
                timeTv.setText(time);
                // 保存时间信息到记账对象
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }

    /* 显示备注对话框 */
    public void showBZDialog() {
        // 创建备注对话框
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize(); // 设置对话框尺寸

        // 设置确定按钮监听
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                // 获取备注文本
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    // 更新备注显示
                    beizhuTv.setText(msg);
                    // 保存备注到记账对象
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel(); // 关闭对话框
            }
        });
    }
}