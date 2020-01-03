package com.hxd.gobus.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by wangqingbin on 2018/7/17.
 */

public class InputEditText extends EditText {

    private OnInputCompleteListener mOnInputCompleteListener;

    public InputEditText(Context context) {
        super(context);
    }

    public InputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public InputEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused && mOnInputCompleteListener != null) {
            mOnInputCompleteListener.onInputComplete();
        }
    }

    public void setOnInputCompleteListener(OnInputCompleteListener listener) {
        this.mOnInputCompleteListener = listener;
    }

    public interface OnInputCompleteListener {
        void onInputComplete();
    }

}

