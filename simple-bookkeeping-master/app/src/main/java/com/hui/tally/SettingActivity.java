package com.hui.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.hui.tally.db.DBManager;

// 设置页面Activity（提供清除数据功能）
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置布局文件（activity_setting.xml）
        setContentView(R.layout.activity_setting);
    }

    // 点击事件处理
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting_iv_back:  // 返回按钮
                finish();  // 结束当前Activity
                break;
            case R.id.setting_tv_clear:  // 清除数据按钮
                showDeleteDialog();  // 显示删除确认对话框
                break;
        }
    }

    // 显示删除确认对话框
    private void showDeleteDialog() {
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示")  // 设置标题
                .setMessage("您确定要删除所有记录么？\n注意：删除后无法恢复，请慎重选择！")  // 设置提示信息
                .setPositiveButton("取消", null)  // 设置取消按钮（原代码中"取消"是PositiveButton）
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {  // 设置确定按钮（原代码中"确定"是NegativeButton）
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 删除数据库中的所有记录
                        DBManager.deleteAllAccount();
                        // 显示删除成功提示
                        Toast.makeText(SettingActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();  // 创建并显示对话框
    }
}