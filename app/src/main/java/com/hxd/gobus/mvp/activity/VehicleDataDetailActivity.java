package com.hxd.gobus.mvp.activity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.RealTimeData;
import com.hxd.gobus.mvp.contract.IVehicleDataDetailContract;
import com.hxd.gobus.mvp.presenter.VehicleDataDetailPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 车辆实时数据
 * @author: wangqingbin
 * @date: 2019/8/15 19:34
 */

public class VehicleDataDetailActivity extends BaseActivity<VehicleDataDetailPresenter> implements IVehicleDataDetailContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_data_detail_name)
    TextView tv_vehicle_data_detail_name;

    @BindView(R.id.tv_vehicle_data_detail_license)
    TextView tv_vehicle_data_detail_license;

    @BindView(R.id.tv_vehicle_data_detail_electricity)
    TextView tv_vehicle_data_detail_electricity;

    @BindView(R.id.tv_vehicle_data_detail_life_mileage)
    TextView tv_vehicle_data_detail_life_mileage;

    @BindView(R.id.tv_vehicle_data_detail_total_mileage)
    TextView tv_vehicle_data_detail_total_mileage;

    @BindView(R.id.tv_vehicle_data_detail_full_time)
    TextView tv_vehicle_data_detail_full_time;

    @BindView(R.id.tv_vehicle_data_detail_hundred_speed)
    TextView tv_vehicle_data_detail_hundred_speed;

    @BindView(R.id.tv_vehicle_data_detail_hundred_gasoline)
    TextView tv_vehicle_data_detail_hundred_gasoline;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                break;
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_data_detail;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);

        mTvRightTitle.setVisibility(View.GONE);
    }

    @Override
    protected void doBusiness() {
        mPresenter.getIntentData(getIntent());
    }

    @Override
    public void initRealTimeData(RealTimeData realTimeData) {
        if(!TextUtils.isEmpty(realTimeData.getVehicleMark())){
            mTvCenterTitle.setText(realTimeData.getVehicleMark());
        }else{
            mTvCenterTitle.setText("车辆实时车况");
        }
        tv_vehicle_data_detail_name.setText(realTimeData.getVehicleName());
        tv_vehicle_data_detail_license.setText(realTimeData.getVehicleMark());
        if (realTimeData.getSoc() != null){
            tv_vehicle_data_detail_electricity.setText(realTimeData.getSoc().toString()+"%");
        }else{
            tv_vehicle_data_detail_electricity.setText("未知");
        }
        if (realTimeData.getXhMileage() != null){
            tv_vehicle_data_detail_life_mileage.setText(realTimeData.getXhMileage().toString()+"KM");
        }else{
            tv_vehicle_data_detail_life_mileage.setText("未知");
        }
        if (realTimeData.getMileage() != null){
            tv_vehicle_data_detail_total_mileage.setText(realTimeData.getMileage().toString()+"KM");
        }else{
            tv_vehicle_data_detail_total_mileage.setText("未知");
        }
        tv_vehicle_data_detail_full_time.setText("未知");
//        tv_vehicle_data_detail_hundred_speed.setText("未知");
//        tv_vehicle_data_detail_hundred_gasoline.setText("未知");
    }
}
