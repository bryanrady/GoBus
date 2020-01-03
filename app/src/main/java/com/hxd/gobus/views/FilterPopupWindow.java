package com.hxd.gobus.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.hxd.gobus.R;
import com.hxd.gobus.utils.DateUtil;
import com.hxd.gobus.utils.ToastUtils;
import com.hxd.gobus.views.datepicker.DateUtils;
import com.hxd.gobus.views.datepicker.SelectDateTimePop;
import com.lxj.xpopup.impl.PartShadowPopupView;
import java.text.ParseException;

/**
 * @author: wangqingbin
 * @date: 2019/12/9 17:04
 */
public class FilterPopupWindow extends PartShadowPopupView implements View.OnClickListener {

    private Activity mContext;
    private OnFilterSureListener mListener;
    private EditText mEtVehicleMark;
    private TextView mTvStartDate;
    private TextView mTvEndDate;
    private Button mBtnReset;
    private Button mBtnSure;
    private String startDate;
    private String endDate;

    public FilterPopupWindow(@NonNull Context context) {
        super(context);
        mContext = (Activity) context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_filter_popup_window;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mEtVehicleMark = findViewById(R.id.et_filter_vehicle_mark);
        mTvStartDate = findViewById(R.id.tv_filter_start_date);
        mTvEndDate = findViewById(R.id.tv_filter_end_date);
        mBtnReset = findViewById(R.id.btn_filter_reset);
        mBtnSure = findViewById(R.id.btn_filter_sure);
        mTvStartDate.setOnClickListener(this);
        mTvEndDate.setOnClickListener(this);
        mBtnReset.setOnClickListener(this);
        mBtnSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_filter_start_date:
                SelectDateTimePop selectDateTimePop = new SelectDateTimePop(mContext, mTvStartDate, true, false);
                selectDateTimePop.setOnDateTimeSetListener(new SelectDateTimePop.OnDateTimeSetListener() {
                    @Override
                    public void OnDateTimeSet(long date, int i, View v) throws ParseException {
                        try {
                            String time = DateUtils.longToString(date, DateUtil.FORMAT_3);
                            startDate = time;
                            if(!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)){
                                if(DateUtil.equalsDate(startDate,endDate) == 1){
                                    ToastUtils.showShortToast(mContext,"结束日期应大于等于起始日期!");
                                    return;
                                }
                            }
                            mTvStartDate.setText(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.tv_filter_end_date:
                selectDateTimePop = new SelectDateTimePop(mContext, mTvEndDate, true, false);
                selectDateTimePop.setOnDateTimeSetListener(new SelectDateTimePop.OnDateTimeSetListener() {
                    @Override
                    public void OnDateTimeSet(long date, int i, View v) throws ParseException {
                        try {
                            String time = DateUtils.longToString(date, DateUtil.FORMAT_3);
                            endDate = time;
                            if(!TextUtils.isEmpty(startDate) && !TextUtils.isEmpty(endDate)){
                                if(DateUtil.equalsDate(startDate,endDate) == 1){
                                    ToastUtils.showShortToast(mContext,"结束日期应大于等于起始日期!");
                                    return;
                                }
                            }
                            mTvEndDate.setText(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case R.id.btn_filter_reset:
                if(!TextUtils.isEmpty(startDate)){
                    startDate = null;
                }
                if(!TextUtils.isEmpty(endDate)){
                    endDate = null;
                }
                mEtVehicleMark.setText("");
                mTvStartDate.setText("");
                mTvEndDate.setText("");
                if(mListener != null){
                    mListener.onReset();
                }
                break;
            case R.id.btn_filter_sure:
                String vehicleMark = mEtVehicleMark.getText().toString().trim();
                String startDate = mTvStartDate.getText().toString().trim();
                String endDate = mTvEndDate.getText().toString().trim();
                if(TextUtils.isEmpty(vehicleMark) && TextUtils.isEmpty(startDate) && TextUtils.isEmpty(endDate)){
                    ToastUtils.showShortToast(mContext,"筛选条件不能为空!");
                    return;
                }
                if (mListener != null){
                    mListener.onSure(vehicleMark,startDate,endDate);
                }
                dismiss();
                break;
        }
    }

    public interface OnFilterSureListener{
        void onSure(String vehicleMark,String startDate,String endDate);
        void onReset();
    }

    public void setOnFilterSureListener(OnFilterSureListener listener){
        this.mListener = listener;
    }

    public void setRefresh(){
        if(mEtVehicleMark != null){
            mEtVehicleMark.setText("");
        }
        if(mTvStartDate != null){
            mTvStartDate.setText("");
        }
        if(mTvEndDate != null){
            mTvEndDate.setText("");
        }
    }
}
