package com.hxd.gobus.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;

/**
 * 位置信息
 * Created by wangqingbin on 2018/5/31.
 */

public class LocationMessageActivity extends BaseActivity implements View.OnClickListener,GeocodeSearch.OnGeocodeSearchListener{

    private MapView mMapView;
    private TextView tv_message_aoiname;
    private TextView tv_message_address;

    private Toolbar mToolbar;
    private LinearLayout llBack;//返回
    private TextView tvToolbarTitle;//标题
    private TextView tvRightTitle;  //右边标题

    private AMap aMap;
    private double longitude;
    private double latitude;
    private String aoiname;

    private UiSettings mUiSettings;
    private MarkerOptions markerOption;
    private Marker marker;
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public int bindLayout() {
        return R.layout.activity_location_message;
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        llBack = findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvRightTitle.setVisibility(View.VISIBLE);
        tvRightTitle.setText("导航");
        tvRightTitle.setOnClickListener(this);
        tvToolbarTitle.setText("位置信息");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);

        mMapView = findViewById(R.id.message_mapView);
        tv_message_aoiname = findViewById(R.id.tv_message_aoiname);
        tv_message_address = findViewById(R.id.tv_message_address);
    }

    @Override
    public void doBusiness() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置地图logo显示在右下方
            mUiSettings.setZoomControlsEnabled(false);
        }

        latitude = getIntent().getDoubleExtra("latitude",0);
        longitude = getIntent().getDoubleExtra("longitude",0);
        aoiname = getIntent().getStringExtra("locDesc");

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        //逆地理编码
        getReAddress(new LatLonPoint(latitude,longitude));

        initCurPositionMaker(latitude,longitude);

        tv_message_aoiname.setText(aoiname);

    }

    private void gotiGaoDeMap(){
        /**
         * 获取打开高德地图应用uri
         * style
         *导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5
         *不走高速且避免收费；6 不走高速且躲避拥堵；
         *7 躲避收费和拥堵；8 不走高速躲避收费和拥堵)
         */
        try {
            Intent intent = new Intent();
            intent.setPackage("com.autonavi.minimap");
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            //  String url = "amapuri://route/plan/?sid=BGVIS1&slat=39.92848272&slon=116.39560823&sname=A&did=BGVIS2&dlat=39.98848272&dlon=116.47560823&dname=B&dev=0&t=0";
            intent.setData(Uri.parse("androidamap://route?sourceApplication=com.hxd.gobus&dlat=" + latitude + "&dlon=" + longitude + "&dname="+ aoiname + "&dev=0&t=0"));
            startActivity(intent);
        }catch (Exception e){
            showShortToast("请先安装高德地图，否则不能进行导航！");
        }
    }

    /**
     * 响应逆地理编码
     */
    private void getReAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }

    /**
     * 给自己当前位置添加matker图标
     * @param latitude
     * @param longitude
     */
    private void initCurPositionMaker(double latitude,double longitude){
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(latitude,longitude), 18, 30, 10)));
        //    aMap.clear();

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(latitude,longitude));
//        markerOption.title(centerPoint);
        markerOption.draggable(true);
        //默认的marker
//        markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //    自定义的marker
        markerOption.icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_my_location));
        marker = aMap.addMarker(markerOption);
//        marker.showInfoWindow();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_toolbar_back:
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                gotiGaoDeMap();
                break;
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        String address = null;
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                address = result.getRegeocodeAddress().getFormatAddress()+ "附近";
            //    List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                if(address == null){
                    address = "未知位置";
                }
            } else {
                address = "未知位置";
            }
        } else {
            address = "未知位置";
        }
        tv_message_address.setText(address);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
