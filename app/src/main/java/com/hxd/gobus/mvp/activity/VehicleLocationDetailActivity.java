package com.hxd.gobus.mvp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.bean.RealTimeMonitoring;
import com.hxd.gobus.mvp.contract.IVehicleLocationDetailContract;
import com.hxd.gobus.mvp.presenter.VehicleLocationDetailPresenter;
import com.hxd.gobus.utils.AMapUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * https://lbs.amap.com/api/android-sdk/guide/draw-on-map/draw-polyline
 *
 * https://blog.csdn.net/c10WTiybQ1Ye3/article/details/78126082
 * https://www.cnblogs.com/zhangqie/p/8550582.html 轨迹
 * 车辆位置信息
 * @author: wangqingbin
 * @date: 2019/8/15 19:34
 */

public class VehicleLocationDetailActivity extends BaseActivity<VehicleLocationDetailPresenter> implements
        IVehicleLocationDetailContract.View, AMap.OnMapTouchListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ll_toolbar_back)
    LinearLayout mllBack;

    @BindView(R.id.tv_toolbar_center_title)
    TextView mTvCenterTitle;

    @BindView(R.id.tv_toolbar_right_title)
    TextView mTvRightTitle;

    @BindView(R.id.tv_vehicle_location_detail_name)
    TextView tv_vehicle_location_detail_name;

    @BindView(R.id.tv_vehicle_location_detail_license)
    TextView tv_vehicle_location_detail_license;

    @BindView(R.id.tv_vehicle_location_detail_electricity)
    TextView tv_vehicle_location_detail_electricity;

    @BindView(R.id.tv_vehicle_location_detail_use_people)
    TextView tv_vehicle_location_detail_use_people;

    @BindView(R.id.tv_vehicle_location_detail_destination)
    TextView tv_vehicle_location_detail_destination;

    @BindView(R.id.tv_vehicle_location_detail_look_travel_task)
    TextView tv_vehicle_location_detail_look_travel_task;

    @BindView(R.id.vehicle_location_detail_mapView)
    MapView mMapView;

    @BindView(R.id.vehicle_location_detail_scrollview)
    ScrollView mScrollView;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_vehicle_location_detail_look_travel_task})
    void doClick(View view){
        switch (view.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_vehicle_location_detail_look_travel_task:
                if(!"0".equals(mUserCarStatus)){
                    if (mManagementId != null){
                        Intent intent = new Intent(this,TravelTaskActivity.class);
                        intent.putExtra("managementId",mManagementId);
                        startActivity(intent);
                    }else{
                        showShortToast("该车暂时没有出行任务");
                    }
                }else{
                    showShortToast("该车辆暂无使用信息");
                }
                break;
        }
    }

    private AMap mAMap;
    private UiSettings mUiSettings;
    private MarkerOptions mMarkerOption;
    private Marker mMarker;
    private List<LatLng> mLatLngList;
    private Integer mManagementId;
    private String mUserCarStatus;

    @Override
    protected int bindLayout() {
        return R.layout.activity_vehicle_loaction_detail;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    @Override
    protected void doBusiness() {
        setUpMap();
        mPresenter.getIntentData(getIntent());
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mllBack.setVisibility(View.VISIBLE);

        mTvRightTitle.setVisibility(View.GONE);
    }

    private void setUpMap(){
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
            mUiSettings.setZoomControlsEnabled(false);

            mAMap.setOnMapTouchListener(this);
        }
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mScrollView.requestDisallowInterceptTouchEvent(false);
        } else {
            mScrollView.requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mMapView != null){
            mMapView.onCreate(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMapView != null){
            mMapView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMapView != null){
            mMapView.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMapView != null){
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null){
            mMapView.onDestroy();
        }
    }

    private void drawPolyline() {
        mLatLngList = showListLat();
        //起点位置和  地图界面大小控制
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLngList.get(0), 7));
    //    mAMap.setMapTextZIndex(2);
        // 绘制一条带有纹理的直线
        mAMap.addPolyline((new PolylineOptions())
                //手动数据测试
                //.add(new LatLng(26.57, 106.71),new LatLng(26.14,105.55),new LatLng(26.58, 104.82), new LatLng(30.67, 104.06))
                //集合数据
                .addAll(mLatLngList)
                //线的宽度
                .width(10).setDottedLine(false).geodesic(false)
                //颜色
                .color(Color.argb(255,255,20,147)));

        LatLonPoint latLonPoint = new LatLonPoint(30.67,104.06);

        //起点图标
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(latLonPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_start)));

        //终点坐标
        LatLonPoint latLonPointEnd = new LatLonPoint(29.89, 107.7);
        mAMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(latLonPointEnd))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.amap_end)));
    }

    private List<LatLng> showListLat(){
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < coords.length; i += 2) {
            points.add(new LatLng(coords[i+1], coords[i]));
        }
        return points;
    }

    private final double[] coords = {
            104.06, 30.67,
            104.32, 30.88,
            104.94, 30.57,
            103.29, 30.2,
            103.81, 30.97,
            104.73, 31.48,
            106.06, 30.8,
            107.7, 29.89
    };

    @Override
    public void initRealTimeMonitoring(RealTimeMonitoring realTimeMonitoring) {
        if(!TextUtils.isEmpty(realTimeMonitoring.getVehicleMark())){
            mTvCenterTitle.setText(realTimeMonitoring.getVehicleMark()+" 位置");
        }else{
            mTvCenterTitle.setText("车辆实时位置");
        }
        mManagementId = realTimeMonitoring.getManagementId();
        mUserCarStatus = realTimeMonitoring.getUseCarStatus();
        tv_vehicle_location_detail_name.setText(realTimeMonitoring.getVehicleName());
        tv_vehicle_location_detail_license.setText(realTimeMonitoring.getVehicleMark());
        if (realTimeMonitoring.getBattery() != null){
            tv_vehicle_location_detail_electricity.setText(realTimeMonitoring.getBattery().toString());
        }
        if("0".equals(realTimeMonitoring.getUseCarStatus())){
            tv_vehicle_location_detail_use_people.setText("未领取");
        }else if("1".equals(realTimeMonitoring.getUseCarStatus())){
            if(!TextUtils.isEmpty(realTimeMonitoring.getReceiveCarPersonName())){
                tv_vehicle_location_detail_use_people.setText(realTimeMonitoring.getReceiveCarPersonName());
            }else{
                tv_vehicle_location_detail_use_people.setText("使用中");
            }
        }else if("2".equals(realTimeMonitoring.getUseCarStatus())){
            tv_vehicle_location_detail_use_people.setText("已归还");
        }
    //    tv_vehicle_location_detail_destination.setText("");

        BigDecimal lat = realTimeMonitoring.getLat();
        BigDecimal lng = realTimeMonitoring.getLng();

        if (lat != null && lng != null){
            initMaker(lat.doubleValue(),lng.doubleValue());
        }

    }

    private void changeCamera(CameraUpdate update) {
        if (mAMap != null){
            mAMap.moveCamera(update);
        }
    }

    private void initMaker(double latitude,double longitude){
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(latitude,longitude), 15, 30, 30)));
        //    aMap.clear();

        mMarkerOption = new MarkerOptions();
        mMarkerOption.position(new LatLng(latitude,longitude));
        //    markerOption.title(aoiName);
        mMarkerOption.draggable(true);
        //默认的marker
        //    markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //    自定义的marker
        mMarkerOption.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_my_location));
        mMarker = mAMap.addMarker(mMarkerOption);
//        marker.showInfoWindow();
    }
}
