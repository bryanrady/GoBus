package com.hxd.gobus.views.datepicker;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.hxd.gobus.R;

import java.text.ParseException;
import java.util.Calendar;

public class SelectDateTimePop extends PopupWindow implements OnClickListener {

    private Button btnCancel, btnConfirm;
    private DateTimeYearPicker mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private OnDateTimeSetListener mOnDateTimeSetListener;
    private Activity context;
    private View parent;
    private LinearLayout title;
    private RelativeLayout bottom;
    private int AP = 0;

    /**
     * show：显示标题(窗口居中)；hide：隐藏标题（窗口位于底部）
     */
    public SelectDateTimePop(final Activity context, View parent, boolean show, boolean last) {
        this.context = context;
        this.parent = parent;
        mDateTimePicker = new DateTimeYearPicker(context, last);
        mDateTimePicker.setOnDateTimeChangedListener(new DateTimeYearPicker.OnDateTimeChangedListener() {

            @Override
            public void onDateTimeChanged(DateTimeYearPicker view, int year, int month, int day, int ap) {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, month);
                mDate.set(Calendar.DAY_OF_MONTH, day);
                mDate.set(Calendar.SECOND, 0);
                AP = ap;
            }

        });
        btnCancel = (Button) mDateTimePicker.findViewById(R.id.btn_cancel);
        btnConfirm = (Button) mDateTimePicker.findViewById(R.id.btn_ok);
        title = (LinearLayout) mDateTimePicker.findViewById(R.id.ll_title);
        bottom = (RelativeLayout) mDateTimePicker.findViewById(R.id.rl_btn);
        btnConfirm.setOnClickListener(this);
        //取消按钮
        btnCancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        if (show) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }
        //设置SelectDateTimePop的View
        this.setContentView(mDateTimePicker);
        //设置SelectDateTimePop弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectDateTimePop弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectDateTimePop弹出窗体可点击
        this.setFocusable(true);
        //设置SelectDateTimePop弹出窗体动画效果
        this.setAnimationStyle(R.style.animation);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    //    translucenceBg();
        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
            //    whiteBg();
            }
        });
    }

    private void translucenceBg() {
        WindowManager.LayoutParams params = context.getWindow()
                .getAttributes();
        params.alpha = 0.7f;

        context.getWindow().setAttributes(params);

    }

    private void whiteBg() {
        WindowManager.LayoutParams params = context.getWindow()
                .getAttributes();
        params.alpha = 1.0f;

        context.getWindow().setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        if (mOnDateTimeSetListener != null) {
            try {
                mOnDateTimeSetListener.OnDateTimeSet(mDate.getTimeInMillis(), AP, parent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnDateTimeSetListener {
        /**
         * 您选择的时间
         *
         * @param date
         */
        void OnDateTimeSet(long date, int i, View v) throws ParseException;
    }

    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
        mOnDateTimeSetListener = callBack;
    }
}