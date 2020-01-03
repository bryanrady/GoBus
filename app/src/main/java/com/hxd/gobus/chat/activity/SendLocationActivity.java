package com.hxd.gobus.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.AMapUtils;
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
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hxd.gobus.R;
import com.hxd.gobus.base.BaseActivity;
import com.hxd.gobus.chat.utils.BitmapLoader;
import com.hxd.gobus.config.BaseConfig;
import com.hxd.gobus.utils.AmapLocationUtil;
import com.hxd.gobus.utils.CharacterParser;
import com.hxd.gobus.views.SearchBox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发送位置信息
 * Created by wangqingbin on 2018/5/31.
 */

public class SendLocationActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener
            ,AMap.OnCameraChangeListener,GeocodeSearch.OnGeocodeSearchListener, AMap.OnMapScreenShotListener{

    @BindView(R.id.location_searchBox)
    SearchBox mSearchBox;

    @BindView(R.id.location_mapView)
    MapView mMapView;

    @BindView(R.id.finetune_lisview)
    ListView finetune_lisview;

    @OnClick({R.id.ll_toolbar_back,R.id.tv_toolbar_right_title})
    void doClick(View v){
        switch (v.getId()) {
            case R.id.ll_toolbar_back:
                intent = new Intent();
                setResult(RESULT_OK,intent);
                defaultFinish();
                break;
            case R.id.tv_toolbar_right_title:
                aMap.getMapScreenShot(this);
                break;
        }
    }

    private Toolbar mToolbar;
    private LinearLayout llBack;//返回
    private TextView tvToolbarTitle;//标题
    private TextView tvRightTitle;

    private AMap aMap;
    private double longitude;
    private double latitude;
    private String aoiName;
    private String address;

    private UiSettings mUiSettings;
    private MarkerOptions markerOption;
    private Marker marker;
    private Intent intent;

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp;
    private PoiSearch poiSearch;
    private List<PoiItem> poiItemList;// poi数据
    private PoiItem poiItem;
    private String keyWord = "";
    private LocationListAdapter locationListAdapter;

    private CharacterParser mParser;

    private LatLng lastLatLng;

    @Override
    public int bindLayout() {
        return R.layout.location_finetune;
    }

    @Override
    public void initView() {
        mToolbar = findViewById(R.id.toolbar);
        llBack = findViewById(R.id.ll_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_center_title);
        tvRightTitle = findViewById(R.id.tv_toolbar_right_title);
        llBack.setVisibility(View.VISIBLE);
        tvRightTitle.setVisibility(View.VISIBLE);

        mSearchBox.setVisibility(View.GONE);
        tvRightTitle.setText("确定");
        tvToolbarTitle.setText("发送位置");
        mToolbar.setTitle("");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void doBusiness() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();
            mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);// 设置地图logo显示在右下方
            mUiSettings.setZoomControlsEnabled(false);
            aMap.setOnCameraChangeListener(this);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20.5f));
        }

        latitude = AmapLocationUtil.getInstance().getLat();
        longitude = AmapLocationUtil.getInstance().getLng();

        finetune_lisview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                locationListAdapter.setCheckposition(position);
                locationListAdapter.notifyDataSetChanged();
                if(poiItemList.size() > position){
                    poiItem = (PoiItem) locationListAdapter.getItem(position);
                    double latitude = poiItem.getLatLonPoint().getLatitude();
                    double longitude = poiItem.getLatLonPoint().getLongitude();

                    mLatitude = latitude;
                    mLongitude = longitude;
                    mStreet = poiItem.getSnippet();

                    LatLng latLng = new LatLng(latitude,longitude);
                    lastLatLng = latLng;
                //    changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(latitude, longitude), 18, 30, 30)));
                    marker.setPosition(latLng);
                }
            }
        });

        mParser = CharacterParser.getInstance();

        //根据输入框输入值的改变来过滤搜索
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());//当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<PoiItem> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = poiItemList;
        } else {
            filterDateList.clear();
            for (PoiItem poiItem : poiItemList) {
                String name = poiItem.getTitle();
                if (name.indexOf(filterStr.toString()) != -1 || mParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(poiItem);
                }
            }
        }
        locationListAdapter.updateData(filterDateList);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private GeocodeSearch geocoderSearch;

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        initMaker(latitude,longitude);

        LatLonPoint latLonPoint = new LatLonPoint(latitude,longitude);
        doSearchQuery(latLonPoint);

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
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
        AmapLocationUtil.getInstance().destroyLocation();
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }

    /**
     * 给地图添加matker图标
     * @param latitude
     * @param longitude
     */
    private void initMaker(double latitude,double longitude){
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        new LatLng(latitude,longitude), 18, 30, 30)));
        //    aMap.clear();

        markerOption = new MarkerOptions();
        markerOption.position(new LatLng(latitude,longitude));
        markerOption.draggable(true);
        //默认的marker
        //    markerOption.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //    自定义的marker
        markerOption.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_my_location));
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(LatLonPoint latLonPoint) {
        if(poiItemList != null){
            poiItemList.clear();
            locationListAdapter.notifyDataSetChanged();
        }
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", AmapLocationUtil.getInstance().getCity());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(30);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
    //    latLonPoint = new LatLonPoint(AmapLocationUtil.getInstance().getLat(), AmapLocationUtil.getInstance().getLng());
        if (latLonPoint != null) {
            poiSearch = new PoiSearch(this, query);
            poiSearch.setOnPoiSearchListener(this);
            // 设置搜索区域为以lp点为圆心，其周围500米范围
            poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 500, true));//
            poiSearch.searchPOIAsyn();// 异步搜索
        }
    }

    @Override
    public void onPoiSearched(PoiResult result, int rcode) {
        if (rcode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    poiItemList = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    if (poiItemList != null && poiItemList.size() > 0) {
                        locationListAdapter = new LocationListAdapter(poiItemList);
                        finetune_lisview.setAdapter(locationListAdapter);
                        locationListAdapter.notifyDataSetChanged();
                    } else {
                        showShortToast("对不起，没有搜索到相关数据！");
                    }
                }
            } else {
                showShortToast("对不起，没有搜索到相关数据！");
            }
        } else {
            showShortToast(rcode);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        /**这个方法很重要，虽然在上述的onLocationChanged方法我们也能获得坐标点的信息但是我们
         通常在这里获得定位坐标的经纬度,获得了经纬度后我们并不能知道该坐标点的具体坐标信息，所以
         需要把对应的坐标点转化为具体地址，这就是所谓的同步逆地理编码请求，和定位是黄金搭档
         */
        mLatitude = cameraPosition.target.latitude;
        mLongitude = cameraPosition.target.longitude;
        LatLonPoint mCurrentPoint = new LatLonPoint(cameraPosition.target.latitude, cameraPosition.target.longitude);
        LatLng mCurrentLatLng = new LatLng(cameraPosition.target.latitude,cameraPosition.target.longitude);

        getReAddress(mCurrentPoint);

        if(lastLatLng != null){
            if(calculatLinearDistance(mCurrentLatLng,lastLatLng) > 100){
                doSearchQuery(mCurrentPoint);
            }
        }else{
            doSearchQuery(mCurrentPoint);
        }
    //    changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(mCurrentLatLng, 18, 30, 30)));
        marker.setPosition(mCurrentLatLng);
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
     * 计算两点之间的直线距离 单位是米
     * @param latLng
     * @param centerlatLng
     * @return
     */
    private int calculatLinearDistance(LatLng latLng, LatLng centerlatLng) {
        float distance = 0;
        if(latLng!=null && centerlatLng!=null){
            distance = Math.abs(AMapUtils.calculateLineDistance(latLng,centerlatLng));
        }
        return (int)distance;
    }


    private double mLatitude;
    private double mLongitude;
    private String mStreet;

    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        if(bitmap != null){
            String fileName = UUID.randomUUID().toString();
            String path = BitmapLoader.saveBitmapToLocal(bitmap, fileName);
            Intent intent = new Intent();
            intent.putExtra("latitude", mLatitude);
            intent.putExtra("longitude", mLongitude);
        //    intent.putExtra("mapview", mMapView.getMapLevel());
            intent.putExtra("street", mStreet);
            intent.putExtra("path", path);
            setResult(BaseConfig.RESULT_CODE_SEND_LOCATION, intent);
            defaultFinish();
        }else{
            showShortToast(getApplication().getString(R.string.send_location_error));
        }

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
//                curAoiName = result.getRegeocodeAddress().getFormatAddress()
//                        + "附近";
                List<PoiItem> pois = result.getRegeocodeAddress().getPois();
                if(pois != null && pois.size()>0){
                    mStreet = pois.get(0).toString()+ "附近";
                }else{
                    mStreet = "未知位置";
                }
            } else {
                mStreet = "未知位置";
            }
        } else {
            mStreet = "未知位置";
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    class LocationListAdapter extends BaseAdapter {

        private int checkPosition = -1;
        private List<PoiItem> poiItemList;

        public LocationListAdapter(List<PoiItem> poiItemList){
            this.poiItemList = poiItemList;
        }

        public void setCheckposition(int checkPosition){
            this.checkPosition = checkPosition;
        }

        public void updateData(List<PoiItem> poiItemList){
            this.poiItemList = poiItemList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return poiItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return poiItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SendLocationActivity.this).inflate(R.layout.listview_location_item, null);

                holder.tv_location_title = (TextView) convertView.findViewById(R.id.tv_location_title);
                holder.tv_location_snippet = (TextView) convertView.findViewById(R.id.tv_location_snippet);
                holder.image_location_checked = (ImageView) convertView.findViewById(R.id.image_location_checked);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }

            holder.tv_location_title.setText(poiItemList.get(position).getTitle());
            holder.tv_location_snippet.setText(poiItemList.get(position).getSnippet());
            if(checkPosition == position){
                holder.image_location_checked.setVisibility(View.VISIBLE);
            }else{
                holder.image_location_checked.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    class ViewHolder{
        TextView tv_location_title;
        TextView tv_location_snippet;
        ImageView image_location_checked;
    }

}

