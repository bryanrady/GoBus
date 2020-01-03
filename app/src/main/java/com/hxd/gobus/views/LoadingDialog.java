package com.hxd.gobus.views;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;

/**
 * Created by pengyu520 on 2016/6/24.
 */
public class LoadingDialog extends Dialog {
    private Context context = null;
    private Dialog mDialog = null;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 显示LoadingDialog
     *
     * @param msg        提示信息
     * @param cancelable 点击外部是否消失
     */
    public void showDialog(CharSequence msg, boolean cancelable) {
        View view =  LayoutInflater.from(context).inflate(R.layout.loading_dialog, null);// 得到加载view
        ImageView ivProgress = (ImageView) view.findViewById(R.id.iv_progress);//动画图片
        CardView cvDialog = (CardView) view.findViewById(R.id.cv_dialog);// 加载布局
        TextView tvTip = (TextView) view.findViewById(R.id.tv_tip);// 提示文字
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation); // 加载动画
        ivProgress.startAnimation(hyperspaceJumpAnimation);// 使用ImageView显示动画
        tvTip.setText(msg);// 设置加载信息
        mDialog = new LoadingDialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        mDialog.setCancelable(cancelable);
        mDialog.setContentView(cvDialog,new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        mDialog.show();
    }

    /**
     * 关闭LoadingDialog
     */
    public void closeDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
