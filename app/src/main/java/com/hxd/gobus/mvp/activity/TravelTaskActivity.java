package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.Attach;
import com.hxd.gobus.bean.Vehicle;
import com.hxd.gobus.mvp.adapter.AttachLookRecyclerAdapter;
import com.hxd.gobus.mvp.contract.ITravelTaskContract;
import com.hxd.gobus.mvp.presenter.TravelTaskPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author: wangqingbin
 * @date: 2019/9/23 17:52
 */

public class TravelTaskActivity extends BaseActivity<TravelTaskPresenter> implements
        ITravelTaskContract.View,AttachLookRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_travel_task_apply_name)
    TextView tv_travel_task_apply_name;

    @BindView(R.id.tv_travel_task_apply_time)
    TextView tv_travel_task_apply_time;

    @BindView(R.id.tv_travel_task_destination)
    TextView tv_travel_task_destination;

    @BindView(R.id.tv_travel_task_vehicle_type)
    TextView tv_travel_task_vehicle_type;

    @BindView(R.id.tv_travel_task_vehicle_range)
    TextView tv_travel_task_vehicle_range;

    @BindView(R.id.tv_travel_task_vehicle_reason)
    TextView tv_travel_task_vehicle_reason;

    @BindView(R.id.tv_travel_task_vehicle_license)
    TextView tv_travel_task_vehicle_license;

    @BindView(R.id.tv_travel_task_whether_use_car)
    TextView tv_travel_task_whether_use_car;

    @BindView(R.id.tv_travel_task_predict_vehicle_time)
    TextView tv_travel_task_predict_vehicle_time;

    @BindView(R.id.tv_travel_task_vehicle_duration)
    TextView tv_travel_task_vehicle_duration;

    @BindView(R.id.tv_travel_task_designated_driver)
    TextView tv_travel_task_designated_driver;

    @BindView(R.id.tv_travel_task_accompany_person)
    TextView tv_travel_task_accompany_person;

    @BindView(R.id.tv_travel_task_get_car)
    TextView tv_travel_task_get_car;

    @BindView(R.id.tv_travel_task_get_car_time)
    TextView tv_travel_task_get_car_time;

    @BindView(R.id.tv_travel_task_return_car)
    TextView tv_travel_task_return_car;

    @BindView(R.id.tv_travel_task_return_car_time)
    TextView tv_travel_task_return_car_time;

    @BindView(R.id.tv_travel_task_remark)
    TextView tv_travel_task_remark;

    @BindView(R.id.ll_attach)
    LinearLayout ll_attach;

    @BindView(R.id.attach_recyclerView)
    RecyclerView mRecyclerView;

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

    private AttachLookRecyclerAdapter mAdapter;

    @Override
    protected int bindLayout() {
        return R.layout.activity_travel_task;
    }

    @Override
    protected void initView() {
        initToolbar();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);
        mTvCenterTitle.setText("本次用车信息");
        mTvRightTitle.setVisibility(View.GONE);
        mTvRightTitle.setText("查看附件");
    }

    @Override
    protected void doBusiness() {
        mPresenter.getIntentData(getIntent());
    }

    @Override
    public void initTravelTaskData(Vehicle vehicle) {
        tv_travel_task_apply_name.setText(vehicle.getApplyPersonName());
        if (vehicle.getApplyDate() != null){
            if (vehicle.getApplyDate().contains(" ")){
                String[] split = vehicle.getApplyDate().split(" ");
                tv_travel_task_apply_time.setText(split[0]);
            }else{
                tv_travel_task_apply_time.setText(vehicle.getApplyDate());
            }
        }
        tv_travel_task_destination.setText(vehicle.getDestination());
        tv_travel_task_vehicle_type.setText(vehicle.getUseCarTypeName());
        tv_travel_task_vehicle_range.setText(vehicle.getUseCarRangeName());
        tv_travel_task_vehicle_reason.setText(vehicle.getUseReason());
        tv_travel_task_vehicle_license.setText(vehicle.getCarNumber());
        tv_travel_task_whether_use_car.setText(vehicle.getIsUseCarName());
        tv_travel_task_predict_vehicle_time.setText(vehicle.getPredictStartDate());
        tv_travel_task_vehicle_duration.setText(vehicle.getPredictDurationName());
        tv_travel_task_designated_driver.setText(vehicle.getAssignDriverName());
        tv_travel_task_accompany_person.setText(vehicle.getTravelPartner());
        tv_travel_task_get_car.setText(vehicle.getReceiveCarPersonName());
        tv_travel_task_get_car_time.setText(vehicle.getReceiveCarDate());
        tv_travel_task_return_car.setText(vehicle.getBackCarPersonName());
        tv_travel_task_return_car_time.setText(vehicle.getBackCarDate());
        tv_travel_task_remark.setText(vehicle.getRemareks());
    }

    @Override
    public void showAttachList(List<Attach> list) {
        if (list.size() > 0){
            mAdapter = new AttachLookRecyclerAdapter(this,list);
            mAdapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            ll_attach.setPadding(
                    0,
                    (int)getResources().getDimension(R.dimen.dp_16),
                    0,
                    (int)getResources().getDimension(R.dimen.dp_16));
        }
    }

    @Override
    public void onItemImageClick(int position, Attach item) {
        Intent intent = new Intent(this,ImagePreviewActivity.class);
        intent.putExtra("imageName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("imageUrl",item.getAttachAddress());
        startActivity(intent);
    }

    @Override
    public void onItemFileClick(int position, Attach item) {
        Intent intent = new Intent(this,FilePreviewActivity.class);
        intent.putExtra("fileName",item.getAttachName());
        intent.putExtra("nativePath",item.getNativePath());
        intent.putExtra("fileUrl",item.getAttachAddress());
        startActivity(intent);
    }
}
