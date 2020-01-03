package com.hxd.gobus.mvp.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.mvp.adapter.AddressAdapter;
import com.hxd.gobus.utils.DataConversionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * //https://github.com/xiaofuchen/WeixinLocation
 * @author: wangqingbin
 * @date: 2019/9/25 11:32
 */
public class ChooseLocationActivity extends BaseActivity {

    @BindView(R.id.iv_location_back)
    ImageView iv_location_back;

    @BindView(R.id.iv_location_search)
    ImageView iv_location_search;

    @BindView(R.id.btn_location_sure)
    Button btn_location_sure;

    @BindView(R.id.mapView)
    MapView mMapView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_center_location)
    ImageView iv_center_location;

    @BindView(R.id.iv_current_location)
    ImageView iv_current_location;

    @OnClick({R.id.iv_location_back,R.id.iv_location_search,R.id.btn_location_sure,R.id.iv_current_location})
    void doClick(View view){
        switch (view.getId()){
            case R.id.iv_location_back:
                defaultFinish();
                break;
            case R.id.iv_location_search:
                Intent intent = new Intent(this, SearchLocationActivity.class);
                if (mLocation != null){
                    intent.putExtra("locationInfo",mLocation);
                }
                startActivityForResult(intent, REQUEST_CODE_SEARCH);
                break;
            case R.id.btn_location_sure:
                if (null != mList && 0 < mList.size() && null != mAddressAdapter) {
                    int position = mAddressAdapter.getSelectPositon();
                    if (position < 0) {
                        position = 0;
                    } else if (position > mList.size()) {
                        position = mList.size();
                    }
                    PoiItem poiItem = mList.get(position);
                    intent = new Intent();
                    intent.putExtra("latitude", poiItem.getLatLonPoint().getLatitude());
                    intent.putExtra("longitude", poiItem.getLatLonPoint().getLongitude());
                    intent.putExtra("address", poiItem.getTitle());
                    setResult(RESULT_OK, intent);
                    defaultFinish();
//                    Toast.makeText(this, "发送：" + poiItem.getTitle() + "  " + poiItem.getSnippet() + "  " + "纬度：" + poiItem.getLatLonPoint().getLatitude() + "  "
//                            + "经度：" + poiItem.getLatLonPoint().getLongitude(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_current_location:
                iv_current_location.setImageResource(R.mipmap.ic_location_gps_green);
                if (null != mSelectByListMarker) {
                    mSelectByListMarker.setVisible(false);
                }
                if (null == mLocation) {
                    startLocation();
                } else {
                    doWhenLocationSuccess();
                }
                break;
        }
    }

    private AddressAdapter mAddressAdapter;
    private List<PoiItem> mList;
    private PoiItem mUserSelectPoiItem;

    private AMap mAMap;
    private Marker mMarker, mLocationGpsMarker, mSelectByListMarker;
    private UiSettings mUiSettings;
    private PoiSearch mPoiSearch;
    private PoiSearch.Query mQuery;
    private boolean mIsSearchData = false;//是否搜索地址数据
    private int mSearchAllPageNum;//Poi搜索最大页数，可应用于上拉加载更多
    private int mSearchNowPageNum;//当前poi搜索页数
    private float mZoom = 18;//地图缩放级别

    private AMapLocationClient mLocationClient = null;
    private AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    private AMapLocation mLocation;
    private AMapLocationListener mAMapLocationListener;

    private OnPoiSearchListener mOnPoiSearchListener;
    private GeocodeSearch.OnGeocodeSearchListener mOnGeocodeSearchListener;

    private ObjectAnimator mTransAnimator;//地图中心标志动态
    private static final int REQUEST_CODE_SEARCH = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mMapView != null){
            mMapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null){
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
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
        stopLocation();
        if (mMapView != null){
            mMapView.onDestroy();
        }
        if (null != mPoiSearch) {
            mPoiSearch = null;
        }
        if (null != mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_choose_location;
    }

    @Override
    protected void initView() {
        mAMap = mMapView.getMap();

        mUiSettings = mAMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(false);//是否显示地图中放大缩小按钮
        mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//是否显示缩放级别
        mAMap.setMyLocationEnabled(false);// 是否可触发定位并显示定位层

        mList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAddressAdapter);

        mTransAnimator = ObjectAnimator.ofFloat(iv_center_location, "translationY", 0f, -80f, 0f);
        mTransAnimator.setDuration(800);
//        mTransAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    protected void doBusiness() {
        initListener();
        startLocation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
            if (data != null){
                try {
                   mUserSelectPoiItem = data.getParcelableExtra("searchInfo");
                    if (null != mUserSelectPoiItem) {
                        mIsSearchData = false;
                        doSearchQuery(true, "", mLocation.getCity(), mUserSelectPoiItem.getLatLonPoint());
                        moveMapCamera(mUserSelectPoiItem.getLatLonPoint().getLatitude(), mUserSelectPoiItem.getLatLonPoint().getLongitude());
                    //    refreshMark(mUserSelectPoiItem.getLatLonPoint().getLatitude(), mUserSelectPoiItem.getLatLonPoint().getLongitude());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void initListener() {
        //监测地图画面的移动
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (null != mLocation && null != cameraPosition && mIsSearchData) {
                    iv_current_location.setImageResource(R.mipmap.ic_location_gps_black);
                    mZoom = cameraPosition.zoom;
                    if (null != mSelectByListMarker) {
                        mSelectByListMarker.setVisible(false);
                    }
                    getAddressInfoByLatLong(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    startTransAnimator();
//                    doSearchQuery(true, "", location.getCity(), new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude));
                }
                if (!mIsSearchData) {
                    mIsSearchData = true;
                }
            }

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }
        });

        //设置触摸地图监听器
        mAMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mIsSearchData = true;
            }
        });

        //Poi搜索监听器
        mOnPoiSearchListener = new OnPoiSearchListener();

        //逆地址搜索监听器
        mOnGeocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000) {
                    if (regeocodeResult != null) {
                        mUserSelectPoiItem = DataConversionUtils.changeToPoiItem(regeocodeResult);
                        if (null != mList) {
                            mList.clear();
                        }
                        mList.addAll(regeocodeResult.getRegeocodeAddress().getPois());
                        if (null != mUserSelectPoiItem) {
                            mList.add(0, mUserSelectPoiItem);
                        }
                        mAddressAdapter.setList(mList);
                        mAddressAdapter.setSelectPosition(0);
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        };

        //gps定位监听器
        mAMapLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation location) {
                try {
                    if (null != location) {
                        stopLocation();
                        if (location.getErrorCode() == 0) {//可在其中解析amapLocation获取相应内容。
                            mLocation = location;
                            doWhenLocationSuccess();
                        } else {
                            //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                            Log.e("AmapError", "location Error, ErrCode:"
                                    + location.getErrorCode() + ", errInfo:"
                                    + location.getErrorInfo());
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        //recycleview列表监听器
        mAddressAdapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                try {
                    mIsSearchData = false;
                    iv_current_location.setImageResource(R.mipmap.ic_location_gps_black);
                    moveMapCamera(mList.get(position).getLatLonPoint().getLatitude(), mList.get(position).getLatLonPoint().getLongitude());
                    refreshSelectByListMark(mList.get(position).getLatLonPoint().getLatitude(), mList.get(position).getLatLonPoint().getLongitude());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        if (null == mLocationClient) {
            //初始化client
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
            //设置定位参数
            mLocationClient.setLocationOption(getDefaultOption());
            // 设置定位监听
            mLocationClient.setLocationListener(mAMapLocationListener);
        }
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setMockEnable(true);//如果您希望位置被模拟，请通过setMockEnable(true);方法开启允许位置模拟
        return mOption;
    }

    public void startLocation() {
        initLocation();
        // 设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    private void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 当定位成功需要做的事情
     */
    private void doWhenLocationSuccess() {
        mIsSearchData = false;
        mUserSelectPoiItem = DataConversionUtils.changeToPoiItem(mLocation);
        doSearchQuery(true, "", mLocation.getCity(), new LatLonPoint(mLocation.getLatitude(), mLocation.getLongitude()));
        moveMapCamera(mLocation.getLatitude(), mLocation.getLongitude());
        refreshLocationMark(mLocation.getLatitude(), mLocation.getLongitude());
    }

    /**
     * 移动动画
     */
    private void startTransAnimator() {
        if (null != mTransAnimator && !mTransAnimator.isRunning()) {
            mTransAnimator.start();
        }
    }

    /**
     * 把地图画面移动到定位地点(使用moveCamera方法没有动画效果)
     *
     * @param latitude
     * @param longitude
     */
    private void moveMapCamera(double latitude, double longitude) {
        if (null != mAMap) {
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), mZoom));
        }
    }

    /**
     * 刷新地图标志物位置
     *
     * @param latitude
     * @param longitude
     */
    private void refreshMark(double latitude, double longitude) {
        if (mMarker == null) {
            mMarker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), android.R.color.transparent)))
                    .draggable(true));
        }
        mMarker.setPosition(new LatLng(latitude, longitude));
        mAMap.invalidate();

    }

    /**
     * 刷新地图标志物gps定位位置
     *
     * @param latitude
     * @param longitude
     */
    private void refreshLocationMark(double latitude, double longitude) {
        if (mLocationGpsMarker == null) {
            mLocationGpsMarker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.ic_location_blue)))
                    .draggable(true));
        }
        mLocationGpsMarker.setPosition(new LatLng(latitude, longitude));
        mAMap.invalidate();

    }

    /**
     * 刷新地图标志物选中列表的位置
     *
     * @param latitude
     * @param longitude
     */
    private void refreshSelectByListMark(double latitude, double longitude) {
        if (mSelectByListMarker == null) {
            mSelectByListMarker = mAMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.ic_location_red)))
                    .draggable(true));
        }
        mSelectByListMarker.setPosition(new LatLng(latitude, longitude));
        if (!mSelectByListMarker.isVisible()) {
            mSelectByListMarker.setVisible(true);
        }
        mAMap.invalidate();

    }

    /**
     * 开始进行poi搜索
     *
     * @param isReflsh 是否为刷新数据
     * @param keyWord
     * @param city
     * @param lpTemp
     */
    protected void doSearchQuery(boolean isReflsh, String keyWord, String city, LatLonPoint lpTemp) {
        mQuery = new PoiSearch.Query(keyWord, "", city);//第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        mQuery.setPageSize(30);// 设置每页最多返回多少条poiitem
        if (isReflsh) {
            mSearchNowPageNum = 0;
        } else {
            mSearchNowPageNum++;
        }
        if (mSearchNowPageNum > mSearchAllPageNum) {
            return;
        }
        mQuery.setPageNum(mSearchNowPageNum);// 设置查第一页


        mPoiSearch = new PoiSearch(this, mQuery);
        mOnPoiSearchListener.setIsRefresh(isReflsh);
        mPoiSearch.setOnPoiSearchListener(mOnPoiSearchListener);
        if (lpTemp != null) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(lpTemp, 10000, true));//该范围的中心点-----半径，单位：米-----是否按照距离排序
        }
        mPoiSearch.searchPOIAsyn();// 异步搜索
    }


    /**
     * 通过经纬度获取当前地址详细信息，逆地址编码
     *
     * @param latitude
     * @param longitude
     */
    private void getAddressInfoByLatLong(double latitude, double longitude) {
        GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        /*
        point - 要进行逆地理编码的地理坐标点。
        radius - 查找范围。默认值为1000，取值范围1-3000，单位米。
        latLonType - 输入参数坐标类型。包含GPS坐标和高德坐标。 可以参考RegeocodeQuery.setLatLonType(String)
        */
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(latitude, longitude), 3000, GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
        geocodeSearch.setOnGeocodeSearchListener(mOnGeocodeSearchListener);
    }

    class OnPoiSearchListener implements PoiSearch.OnPoiSearchListener {

        private boolean mIsRefresh;//是为下拉刷新，否为上拉加载更多

        public void setIsRefresh(boolean isRefresh) {
            this.mIsRefresh = isRefresh;
        }

        @Override
        public void onPoiSearched(PoiResult result, int i) {
            if (i == 1000) {
                if (result != null && result.getQuery() != null) {// 搜索poi的结果
                    mSearchAllPageNum = result.getPageCount();
                    if (result.getQuery().equals(mQuery)) {// 是否是同一条
                        if (mIsRefresh && null != mList) {
                            mList.clear();
                            if (null != mUserSelectPoiItem) {
                                mList.add(0, mUserSelectPoiItem);
                            }
                        }
                        mList.addAll(result.getPois());// 取得第一页的poiitem数据，页数从数字0开始
                        if (null != mAddressAdapter) {
                            mAddressAdapter.setList(mList);
                            mAddressAdapter.setSelectPosition(0);
                            mRecyclerView.smoothScrollToPosition(0);
                        }
                    }
                }
            }
        }

        @Override
        public void onPoiItemSearched(PoiItem poiItem, int i) {

        }
    }
}
