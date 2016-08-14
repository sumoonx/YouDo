package cn.edu.seu.udo.mvp.presenter;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Author: Jeremy Xu on 2016/6/23 10:49
 * E-mail: jeremy_xm@163.com
 */
public class KeyBoardUtil {
    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void openKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     * @param mContext 上下文
     */
    public static void closeKeybord(EditText mEditText, Context mContext)
    {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
