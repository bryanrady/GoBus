package com.hxd.gobus.views;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hxd.gobus.R;

/**
 * Button在Dialog上的实现
 * Created by wangqingbin on 2018/6/27.
 */

public class ButtonDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Button btn1 , btn2;
    private OnClickListener mListener;
    private Dialog mDialog = null;

    public ButtonDialog(Context context){
        super(context);
        this.mContext = context;
    }

    public ButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public void create() {
        View view =  LayoutInflater.from(mContext).inflate(R.layout.button_dialog, null);
        CardView cvDialog = (CardView) view.findViewById(R.id.cv_dialog);// 加载布局
        btn1 = (Button) view.findViewById(R.id.btn_dialog_button1);
        btn2 = (Button) view.findViewById(R.id.btn_dialog_button2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        mDialog = new LoadingDialog(mContext, R.style.loading_dialog);// 创建自定义样式dialog
        mDialog.setCancelable(true);
        mDialog.setContentView(cvDialog,new LinearLayout.LayoutParams( 800,LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
        mDialog.show();
    }

    public void setBtn1Text(String text){
        btn1.setText(text);
    }

    public void setBtn2Text(String text){
        btn2.setText(text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_dialog_button1:
                mListener.onBtnClick(v);
                mDialog.dismiss();
                break;
            case R.id.btn_dialog_button2:
                mListener.onBtnClick(v);
                mDialog.dismiss();
                break;
        }
    }

    public void setOnBtnClickListener(OnClickListener listener){
        mListener = listener;
    }

    public interface OnClickListener{
        void onBtnClick(View view);
    }
}
