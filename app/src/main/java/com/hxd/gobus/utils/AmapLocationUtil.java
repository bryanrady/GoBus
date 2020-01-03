package com.hxd.gobus.utils;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

/**
 * 高德地图定位工具类
 * Created by wangqingbin on 2018/4/19.
 */

public class AmapLocationUtil implements GeocodeSearch.OnGeocodeSearchListener {

    public static AmapLocationUtil instance;

    private AmapLocationUtil(){

    }

    public static AmapLocationUtil getInstance(){
        if(instance == null){
            synchronized (AmapLocationUtil.class){
                if(instance==null){
                    instance = new AmapLocationUtil();
                }
            }
        }
        return instance;
    }

    private GeocodeSearch geocoderSearch;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mOption = null;
    private Context mContext;
    private String address;
    private double lat;
    private double lng;
    private String aoiName;
    private String city;

    private boolean isLocationSuccess = false;   //返回定位是否成功

    public void init(Context context){
        mContext = context;
        mlocationClient = new AMapLocationClient(context);
        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //初始化定位参数
        mOption = getDefaultOption();
        //设置定位参数
        mlocationClient.setLocationOption(mOption);

        //逆地理编码监听
        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(this);

        stopLocation();
        startLocation();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                address = result.getRegeocodeAddress().getFormatAddress() + "附近";
                if(address == null){
                    address = "未知地址";
                }
                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                if(pois != null && pois.size()>0){
                    aoiName = pois.get(0).toString()+ "附近";
                }else{
                    aoiName = "未知位置";
                }
            } else {
                aoiName = "未知位置";
                address = "未知地址";
            }
        } else {
            aoiName = "未知位置";
            address = "未知地址";
        }
        isLocationSuccess = true;
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 响应逆地理编码
     */
    private void getReAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 默认的定位参数
     */
    private AMapLocationClientOption getDefaultOption(){
        if(mOption == null){
            mOption = new AMapLocationClientOption();
            mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
            mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
            mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
            mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
            mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
            mOption.setMockEnable(true);
            mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
            mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
            AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
            mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
            mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
            mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        }
        return mOption;
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0) {
                    lng = location.getLongitude();
                    lat = location.getLatitude();
                    address = location.getAddress();
                    aoiName = location.getAoiName();
                    city = location.getCity();

                    //如果位置是Null,就用逆地理编码
                    if(aoiName == null || address==null){
                        if(lat!=0 && lng!=0){
                            stopLocation();
                            LatLonPoint latLonPoint = new LatLonPoint(lat, lng);
                            getReAddress(latLonPoint);
                        }
                    }else{
                        isLocationSuccess = true;
                    }
                }

            }
        }
    };

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAddress() {
        if(isLocationSuccess){
            return address;
        }
        return "未知地址";
    }

    public String getAoiName() {
        if(isLocationSuccess){
            return aoiName;
        }
        return "未知位置";
    }

    public String getCity(){
        return city;
    }

    public void startLocation(){
        // 启动定位
        mlocationClient.startLocation();
    }

    public void stopLocation(){
        // 停止定位
        mlocationClient.stopLocation();
    }

    public void destroyLocation(){
        if (null != mlocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mlocationClient.onDestroy();
            mlocationClient = null;
            mOption = null;
        }
    }

}
