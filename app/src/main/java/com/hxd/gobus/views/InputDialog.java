package com.hxd.gobus.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hxd.gobus.R;

/**
 * 输入对话框
 */
public class InputDialog {

    private Dialog dialog;
    private AppCompatTextView titleTextView;
    private AppCompatTextView cancelTextView;
    private AppCompatTextView confirmTextView;
    private AppCompatEditText contentEditText;

    public InputDialog(Context context,String title,String content) {
        init(context,title,content);
    }

    public String getTitle() {
        return titleTextView.getText().toString();
    }

    public String getContent() {
        return contentEditText.getText().toString();
    }

    public void setCancelListerner(View.OnClickListener listener) {
        cancelTextView.setOnClickListener(listener);
    }

    public void setConfirmListerner(View.OnClickListener listener) {
        confirmTextView.setOnClickListener(listener);
    }

    private void init(final Context context,String title, String content) {
        this.dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_input);
        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        Window window = dialog.getWindow();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        window.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        layoutParams.width = (int) (displayMetrics.widthPixels * 1);
        window.setAttributes(layoutParams);
        titleTextView = dialog.findViewById(R.id.titleTextView);
        contentEditText = dialog.findViewById(R.id.contentEditText);
        cancelTextView = dialog.findViewById(R.id.cancelTextView);
        confirmTextView = dialog.findViewById(R.id.confirmTextView);
        if(!TextUtils.isEmpty(title)){
            titleTextView.setText(title);
        }
        if(!TextUtils.isEmpty(content)){
            contentEditText.setText(content);
            contentEditText.setSelection(content.length());
        }
        contentEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
