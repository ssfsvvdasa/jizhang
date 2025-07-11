package com.hui.tally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.hui.tally.R;

// 自定义键盘工具类
public class KeyBoardUtils {
    private final Keyboard k1;    // 自定义键盘对象
    private KeyboardView keyboardView;  // 键盘视图容器
    private EditText editText;    // 关联的输入框

    // 键盘完成按钮回调接口
    public interface OnEnsureListener {
        public void onEnsure();
    }

    OnEnsureListener onEnsureListener;  // 回调接口实例

    // 设置完成按钮回调
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    // 构造函数：初始化键盘
    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        // 禁止弹出系统键盘
        this.editText.setInputType(InputType.TYPE_NULL);
        // 加载自定义键盘布局
        k1 = new Keyboard(this.editText.getContext(), R.xml.key);

        // 配置键盘视图
        this.keyboardView.setKeyboard(k1);  // 设置键盘样式
        this.keyboardView.setEnabled(true);  // 启用键盘
        this.keyboardView.setPreviewEnabled(false);  // 禁用按键预览
        // 设置键盘按键监听
        this.keyboardView.setOnKeyboardActionListener(listener);
    }

    // 键盘按键监听实现
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            // 按键按下事件（未实现）
        }

        @Override
        public void onRelease(int primaryCode) {
            // 按键释放事件（未实现）
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            // 获取当前输入框文本和光标位置
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            // 根据按键类型处理
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:   // 删除键
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            // 删除光标前一个字符
                            editable.delete(start - 1, start);
                        }
                    }
                    break;

                case Keyboard.KEYCODE_CANCEL:   // 清零键
                    // 清空输入框
                    editable.clear();
                    break;

                case Keyboard.KEYCODE_DONE:     // 完成键
                    // 触发完成回调
                    if (onEnsureListener != null) {
                        onEnsureListener.onEnsure();
                    }
                    break;

                default:  // 数字键
                    // 在光标处插入字符
                    editable.insert(start, Character.toString((char) primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {
            // 文本输入事件（未实现）
        }

        @Override
        public void swipeLeft() {
            // 向左滑动（未实现）
        }

        @Override
        public void swipeRight() {
            // 向右滑动（未实现）
        }

        @Override
        public void swipeDown() {
            // 向下滑动（未实现）
        }

        @Override
        public void swipeUp() {
            // 向上滑动（未实现）
        }
    };

    // 显示自定义键盘
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        // 如果键盘是隐藏状态，则显示
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    // 隐藏自定义键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        // 如果键盘是可见状态，则隐藏
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
}