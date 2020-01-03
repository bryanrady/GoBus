package com.hxd.gobus.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.hxd.gobus.R;

/**
 * @author: wangqingbin
 * @date: 2019/9/27 9:54
 */

public class BottomPopupDialog extends Dialog implements View.OnClickListener {

    public static final int TYPE_SHOOTING = 1;
    public static final int TYPE_FROM_ALBUM = 2;
    public static final int TYPE_FROM_FILE = 3;

    private OnDialogListener mListener;

    private Button mBtnShooting;
    private Button mBtnFromAlbum;
    private Button mBtnFromFile;
    private Button mBtnCancel;
    private Context mContext;

    public BottomPopupDialog(@NonNull Context context) {
        this(context,R.style.BottomDialog);
    }

    public BottomPopupDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_buttom_popup_window, null, false);
        setContentView(view);
        setCanceledOnTouchOutside(false);

        mBtnShooting = view.findViewById(R.id.button_shooting);
        mBtnFromAlbum = view.findViewById(R.id.button_from_album);
        mBtnFromFile = view.findViewById(R.id.button_from_file);
        mBtnCancel = view.findViewById(R.id.button_cancel);
        mBtnShooting.setOnClickListener(this);
        mBtnFromAlbum.setOnClickListener(this);
        mBtnFromFile.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.width = mContext.getResources().getDisplayMetrics().widthPixels;
        view.measure(0, 0);
        lp.height = view.getMeasuredHeight();

        lp.alpha = 9f;
        dialogWindow.setAttributes(lp);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_shooting:
                if (mListener != null){
                    mListener.onDialogClick(TYPE_SHOOTING);
                }
                break;
            case R.id.button_from_album:
                if (mListener != null){
                    mListener.onDialogClick(TYPE_FROM_ALBUM);
                }
                break;
            case R.id.button_from_file:
                if (mListener != null){
                    mListener.onDialogClick(TYPE_FROM_FILE);
                }
                break;
            case R.id.button_cancel:
                break;
        }
        this.cancel();
    }

    public interface OnDialogListener{
        void onDialogClick(int type);
    }

    public void setOnDialogListener(OnDialogListener listener){
        this.mListener = listener;
    }

}
