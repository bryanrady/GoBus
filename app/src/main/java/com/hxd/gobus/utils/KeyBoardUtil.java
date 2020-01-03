package com.hxd.gobus.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * @author pengyu520
 * @date 2016年5月14日 上午9:53:06
 * @Des:控制系统软键盘开关的工具类
 */
public class KeyBoardUtil {

	/**
	 * 打开软键盘
	 * 
	 * @param context
	 *            上下文
	 * @param textView
	 *            输入框
	 */
	public static void openKeyboard(Context context,TextView textView) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(textView, InputMethodManager.RESULT_SHOWN);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param context
	 *            上下文
	 * @param textView
	 *            输入框
	 */
	public static void closeKeyboard(Context context, TextView textView) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);
	}

	public static boolean isActive(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		return inputMethodManager.isActive();
	}
}
