package com.hxd.gobus.views;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxd.gobus.R;

/**
 * 自定义一个popupwindow
 * Created by wangqingbin on 2018/4/19.
 */

public class CustomPopupWindow extends PopupWindow {

    private View mainView;
    private RelativeLayout layoutRecord, layoutStatistics;
    private TextView tvRecord,tvStatistics;
    private ImageView ivRecord,ivStatistics;

    public CustomPopupWindow(Activity activity, View.OnClickListener listener){
        super(activity);
        mainView = LayoutInflater.from(activity).inflate(R.layout.popwindow_add, null);
        layoutRecord = ((RelativeLayout)mainView.findViewById(R.id.layout_record));
        layoutStatistics = (RelativeLayout)mainView.findViewById(R.id.layout_statistics);
        tvRecord = (TextView)mainView.findViewById(R.id.tv_record);
        tvStatistics = (TextView)mainView.findViewById(R.id.tv_statistics);
        ivRecord = (ImageView)mainView.findViewById(R.id.iv_record);
        ivStatistics = (ImageView)mainView.findViewById(R.id.iv_statistics);
        if (listener != null){
            layoutRecord.setOnClickListener(listener);
            layoutStatistics.setOnClickListener(listener);
        }
        setContentView(mainView);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }

    public void setRecordImg(int resId){
        ivRecord.setImageResource(resId);
    }

    public void setStatisticsImg(int resId){
        ivStatistics.setImageResource(resId);
    }

    public void setRecordText(String text){
        tvRecord.setText(text);
    }

    public void setStatisticsText(String text){
        tvStatistics.setText(text);
    }

    public void setRecordIconVisibility(int visibility){
        ivRecord.setVisibility(visibility);
    }

    public void setStatisticsIconVisibility(int visibility){
        ivStatistics.setVisibility(visibility);
    }
}
